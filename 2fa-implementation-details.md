# Chi tiết triển khai tính năng Xác thực 2 bước (2FA)

Tài liệu này ghi chú lại chi tiết các thành phần, cấu trúc mã nguồn và database đã bị thay đổi/thêm mới trong dự án `yoprj` để hỗ trợ tính năng Xác thực 2 bước bằng ứng dụng Google Authenticator (TOTP).

## 1. Mục đích và Quy trình (Flow)

- Cung cấp thêm 1 lớp bảo vệ phụ cho người dùng sau khi đã xác thực đúng Username/Password.
- Nếu người dùng bật 2FA, tại bước đăng nhập đầu tiên, hệ thống sẽ **không** trả về Access Token ngay mà trả về một **Temporary Token** (có thời hạn ngắn 5 phút) cùng cờ `requires2fa = true`.
- Client dùng `Temporary Token` này kết hợp với mã 6 số (OTP) đọc từ app Google Authenticator gọi API `/login/2fa` để đổi lấy Access Token thực sự.

---

## 2. Thư viện (Dependencies)

Trong file `pom.xml`, dự án đã bổ sung 2 thư viện chuẩn mực cho TOTP:
- `com.j256.two-factor-auth` (v1.3): Dùng để khởi tạo mã Secret Key, Generate URI liên kết và xác minh mã Code (OTP).
- `com.google.zxing:javase` (v3.5.2): Dùng để render URI TOTP thành mã vạch QR Code dạng base64, gửi trực tiếp về Front-End để hiển thị.

---

## 3. Cập nhật Cơ sở dữ liệu & Entity

### Database Migration
Tạo file `V3__add_2fa_fields.sql` trong `src/main/resources/db/migration` để Flyway thực thi việc thêm cột:
```sql
ALTER TABLE users
ADD COLUMN two_factor_secret VARCHAR(255) DEFAULT NULL,
ADD COLUMN is_two_factor_enabled BOOLEAN DEFAULT FALSE NOT NULL;
```

### Entity Update
Trong `User.java` (`com.yo.yoprj.domain.entity.User`):
```java
@Column(name = "two_factor_secret")
private String twoFactorSecret;

@Column(name = "is_two_factor_enabled", nullable = false)
private Boolean isTwoFactorEnabled = false;
```

---

## 4. Các Data Transfer Objects (DTOs) mới

Nằm tại package `com.yo.yoprj.dto.auth`:
- **`TwoFactorSetupResponse`**: Dùng để trả về cho người dùng đoạn văn bản `secret` và chuỗi `qrCodeBase64` để render ảnh QR.
- **`Enable2FaRequest`**: Dùng để người dùng truyền lên đoạn mã `code` OTP gồm 6 chữ số khi họ xác nhận bật 2FA.
- **`Verify2FaRequest`**: Request payload chứa `tempToken` và `code` dùng cho bước cuối cùng lúc Đăng nhập.
- **`AuthResponse` (Được nâng cấp)**: Thêm trường `Boolean requires2fa` và `String tempToken` vào Record cũ.

---

## 5. Tầng Service (Xử lý nghiệp vụ)

### `TwoFactorAuthService` và `TwoFactorAuthServiceImpl` (TẠO MỚI)
- Xử lý việc sinh mã bí mật Base32 (Secret).
- Format URI thành định dạng Google Authenticator: `otpauth://totp/YoEdu:{username}?secret={secret}&issuer=YoEdu`.
- Chuyển URI thành hình ảnh Bitmap QR và sau đó băm thành chuỗi Base64 bằng chuẩn của ZXing.
- Hàm `verifyCode` dùng để check OTP client gửi lên có khớp với khoảng thời gian hiện tại của Secret không (có chênh lệch ± 1 window time).

### `AuthServiceImpl` (CẬP NHẬT)
- **Cập nhật hàm `login()`**: 
  - Đã thêm lệnh `Boolean.TRUE.equals(user.getIsTwoFactorEnabled())` để chia nhánh.
  - Sinh ra token `tempToken` chứa một claim đặc biệt tên là `"type" : "PRE_AUTH_2FA"` để đánh dấu loại Token này chỉ dùng để verify bước 2.
- **Thêm hàm `generate2FaSetup()`**: Khởi tạo thông tin cài đặt 2FA và lưu `secret` tạm vào entity `User`.
- **Thêm hàm `enable2Fa()`**: Xác nhận nếu user nhập đúng OTP lần đầu tiên thì cập nhật cờ `isTwoFactorEnabled = true` cho User.
- **Thêm hàm `verify2FaLogin()`**: Decode `tempToken`, verify claim type `"PRE_AUTH_2FA"`, lấy thông tin User, lấy mã `secret` lưu trong Database so khớp với mã OTP hiện tại. Nếu mọi thứ đúng, gọi tiếp hàm gen JWT đầy đủ.

---

## 6. Endpoints Mới (Tầng Controller)

Ba APIs mới đã được thêm vào `AuthController` (`com.yo.yoprj.controller.AuthController`):

1. `POST /api/auth/2fa/generate` (Cần JWT xác thực, cấp quyền cho user tự tạo Setup)
2. `POST /api/auth/2fa/enable` (Cần JWT xác thực, cấp quyền cho user nhập code lần đầu để bật 2FA)
3. `POST /api/auth/login/2fa` (Public API: Bỏ qua Auth Filter vì dùng `tempToken` trong Body. Đã cấu hình `.permitAll()` tại `SecurityConfig`).

---

## 7. Tài liệu Test

Dự án cung cấp sẵn file `yoedu-2fa.postman_collection.json` đặt ở thư mục gốc. 
Team Frontend hoặc QA có thể nạp thẳng file này vào **Postman**, collection này đã được viết sẵn script để trích xuất Access Token tự động đổ vào biến môi trường, thuận tiện cho việc kiểm thử các bước logic liên hoàn.
