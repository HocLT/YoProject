package com.yo.yoprj.service;

import com.yo.yoprj.dto.auth.TwoFactorSetupResponse;

public interface TwoFactorAuthService {
    
    /**
     * Generates a new 2FA secret and its corresponding QR Code image.
     */
    TwoFactorSetupResponse generateSetup(String username) throws com.yo.yoprj.common.exception.BadRequestException;
    
    /**
     * Verifies the given code against the given secret.
     */
    boolean verifyCode(String secret, String code);
}
