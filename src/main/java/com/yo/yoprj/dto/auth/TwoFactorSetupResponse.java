package com.yo.yoprj.dto.auth;

public record TwoFactorSetupResponse(
        String secret,
        String qrCodeBase64
) {
}
