package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.auth.AuthResponse;
import com.yo.yoprj.dto.auth.CurrentUserResponse;
import com.yo.yoprj.dto.auth.LoginRequest;
import com.yo.yoprj.dto.auth.RegisterRequest;
import com.yo.yoprj.dto.auth.TwoFactorSetupResponse;
import com.yo.yoprj.dto.auth.Verify2FaRequest;
import com.yo.yoprj.dto.auth.Enable2FaRequest;
import com.yo.yoprj.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Login successful", authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        return ApiResponse.success("Registration successful", authService.register(request));
    }

    @GetMapping("/me")
    public ApiResponse<CurrentUserResponse> me(Authentication authentication) throws BadRequestException, NotFoundException {
        return ApiResponse.success(authService.me(authentication.getName()));
    }

    @PostMapping("/login/2fa")
    public ApiResponse<AuthResponse> verify2FaLogin(@Valid @RequestBody Verify2FaRequest request) throws BadRequestException, NotFoundException {
        return ApiResponse.success("2FA Login successful", authService.verify2FaLogin(request));
    }

    @PostMapping("/2fa/generate")
    public ApiResponse<TwoFactorSetupResponse> generate2FaSetup(Authentication authentication) throws BadRequestException, NotFoundException {
        return ApiResponse.success("Generated 2FA setup", authService.generate2FaSetup(authentication.getName()));
    }

    @PostMapping("/2fa/enable")
    public ApiResponse<Void> enable2Fa(Authentication authentication, @Valid @RequestBody Enable2FaRequest request) throws BadRequestException, NotFoundException {
        authService.enable2Fa(authentication.getName(), request);
        return ApiResponse.success("2FA enabled successfully", null);
    }
}
