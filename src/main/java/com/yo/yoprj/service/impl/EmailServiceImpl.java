package com.yo.yoprj.service.impl;

import com.yo.yoprj.domain.entity.Parent;
import com.yo.yoprj.domain.entity.Payment;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPaymentNotification(Payment payment) {
        Student student = payment.getInvoice().getStudent();
        Parent parent = student.getParent();

        if (parent == null || parent.getEmail() == null || parent.getEmail().isBlank()) {
            log.warn("Cannot send payment email: Parent or email is missing for student {}", student.getId());
            return;
        }

        try {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedAmount = currencyFormat.format(payment.getPaidAmount());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(parent.getEmail());
            message.setSubject("YoEdu - Thông báo thanh toán thành công");
            message.setText("Kính gửi phụ huynh " + parent.getFullName() + ",\n\n" +
                    "Hệ thống YoEdu xin thông báo khoản thanh toán của quý vị đã được ghi nhận thành công.\n\n" +
                    "Chi tiết thanh toán:\n" +
                    "- Học sinh: " + student.getFullName() + "\n" +
                    "- Mã phiếu thu: " + payment.getPaymentCode() + "\n" +
                    "- Số tiền: " + formattedAmount + "\n" +
                    "- Mã hóa đơn (Invoice): " + payment.getInvoice().getInvoiceCode() + "\n" +
                    "- Hình thức thanh toán: " + payment.getPaymentMethod() + "\n" +
                    "- Thời gian thanh toán: " + payment.getPaidAt() + "\n\n" +
                    "Cảm ơn quý vị đã tin tưởng và đồng hành cùng YoEdu.\n\n" +
                    "Trân trọng,\nYoEdu Team");

            mailSender.send(message);
            log.info("Sent payment notification email to {}", parent.getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment email to {}: {}", parent.getEmail(), e.getMessage());
        }
    }
}
