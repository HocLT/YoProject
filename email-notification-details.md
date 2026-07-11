# Chi tiết triển khai tính năng Gửi Email Thông báo Thanh toán

Tài liệu này ghi chú chi tiết về kiến trúc, cấu hình và mã nguồn đã được bổ sung/thay đổi để hỗ trợ tính năng gửi email tự động cho phụ huynh ngay sau khi hoàn tất thanh toán hóa đơn học phí trong hệ thống `yoprj`.

## 1. Kiến trúc Bất đồng bộ (Async Event-Driven)

Việc kết nối với SMTP Server (ví dụ: Gmail) để gửi email thường mất từ vài giây đến hơn 10 giây. Nếu đặt đoạn code gửi email trực tiếp vào API `POST /payments`, hệ thống sẽ bị "nghẽn" (blocking) trong khoảng thời gian đó, khiến người dùng cuối cảm thấy ứng dụng xử lý rất chậm.

Để giải quyết vấn đề này, tính năng đã được thiết kế theo kiến trúc **Event-driven (Hướng sự kiện)** kết hợp **Asynchronous (Bất đồng bộ)**:
- **Tách biệt Logic**: API thanh toán chỉ tập trung vào việc tính toán, lưu Database và phát ra (publish) một "sự kiện" (event).
- **Background Task**: Một tiến trình chạy ngầm (Listener) sẽ bắt sự kiện đó và thực hiện việc gửi email một cách độc lập. Người dùng sẽ nhận được thông báo Thanh toán Thành công ngay lập tức trên UI mà không cần chờ email gửi xong.

---

## 2. Thư viện và Cấu hình

### Cập nhật `pom.xml`
Thêm thư viện Spring Boot Mail để tích hợp `JavaMailSender`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

### Cập nhật `application.yaml`
Bổ sung cấu hình kết nối tới máy chủ SMTP của Gmail (port `587` hỗ trợ TLS). Thông tin `username` và `password` (App Password) cần được thay thế bằng tài khoản thật khi chạy production.
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: "your-email@gmail.com"
    password: "your-app-password"
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

---

## 3. Các File / Lớp (Class) được tạo mới

### a) `AsyncConfig.java` (`com.yo.yoprj.config.AsyncConfig`)
- Đánh dấu bằng `@Configuration` và `@EnableAsync`.
- Khai báo cho Spring Boot biết rằng dự án này có sử dụng các hàm chạy đa luồng (background task).

### b) `PaymentCompletedEvent.java` (`com.yo.yoprj.event.PaymentCompletedEvent`)
- Là một Java Record chứa payload duy nhất là đối tượng `Payment` vừa được khởi tạo.
- Đóng vai trò là "gói tin" luân chuyển giữa tiến trình Lưu thanh toán và tiến trình Gửi email.

### c) `EmailService` và `EmailServiceImpl` (`com.yo.yoprj.service.impl`)
- **Nhiệm vụ**: Chịu trách nhiệm hoàn toàn về việc soạn thảo (format tiền tệ, text) và gửi email qua `JavaMailSender`.
- Code tự động lấy email của phụ huynh thông qua chuỗi quan hệ: `payment.getInvoice().getStudent().getParent().getEmail()`.
- Chứa các khối `try-catch` an toàn để nếu gửi email thất bại (ví dụ sai mật khẩu), nó chỉ log ra lỗi (`log.error`) chứ không làm sập tiến trình.

### d) `PaymentEventListener` (`com.yo.yoprj.event.PaymentEventListener`)
- Chứa một hàm `handlePaymentCompletedEvent()` được gắn 2 annotations quan trọng:
  - `@EventListener`: Đăng ký lắng nghe sự kiện `PaymentCompletedEvent` trên toàn cục.
  - `@Async`: Chỉ thị cho Spring Boot ném hàm này sang một Thread (luồng) khác để thực thi, giải phóng Thread chính ngay lập tức.
- Trong hàm này chỉ gọi `emailService.sendPaymentNotification(payment)`.

---

## 4. Những Thay đổi ở Logic Hiện có

### `BillingServiceImpl.java`
Tại phương thức `createPayment()`:
1. Đã Inject thêm `ApplicationEventPublisher eventPublisher`.
2. Sau khi lưu thành công `TuitionInvoice` (Cập nhật số tiền) và `Payment` (Tạo biên lai), đoạn code bổ sung được chạy:
   ```java
   eventPublisher.publishEvent(new PaymentCompletedEvent(savedPayment));
   ```
3. Sau dòng lệnh trên, hàm tiếp tục `return` ra `PaymentResponse` cho API Controller mà không quan tâm Email có được gửi thành công hay không.

## 5. Các bước để kích hoạt Test
1. Tạo **App Password** trên tài khoản Google (Security -> 2-Step Verification -> App Passwords).
2. Dán mật khẩu đó và địa chỉ Gmail của bạn vào `application.yaml`.
3. Kiểm tra DB ở bảng `parents` có trường `email` của phụ huynh chưa.
4. Chạy project và gọi API tạo thanh toán bằng Postman, email sẽ nổ báo về điện thoại của phụ huynh!
