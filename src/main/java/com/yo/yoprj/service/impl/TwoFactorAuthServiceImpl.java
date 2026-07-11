package com.yo.yoprj.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.auth.TwoFactorSetupResponse;
import com.yo.yoprj.service.TwoFactorAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Slf4j
@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    @Value("${spring.application.name:YoEdu}")
    private String issuer;

    @Override
    public TwoFactorSetupResponse generateSetup(String username) throws BadRequestException {
        try {
            // Generate secret
            String secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();

            // Generate URL for QR code
            String qrUrl = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, username, secret, issuer);

            // Generate QR Code image
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            String qrCodeDataUri = "data:image/png;base64," + base64Image;

            return new TwoFactorSetupResponse(secret, qrCodeDataUri);
        } catch (Exception e) {
            log.error("Error generating 2FA setup", e);
            throw new BadRequestException("Could not generate 2FA setup");
        }
    }

    @Override
    public boolean verifyCode(String secret, String code) {
        try {
            int codeInt = Integer.parseInt(code);
            // Verify code, allowing a window of 1 (30 seconds before/after)
            return TimeBasedOneTimePasswordUtil.validateCurrentNumber(secret, codeInt, 10000); 
            // Note: validateCurrentNumber takes secret, authNumber, windowMillis
        } catch (Exception e) {
            log.error("Error verifying 2FA code", e);
            return false;
        }
    }
}
