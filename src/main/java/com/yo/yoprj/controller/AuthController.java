package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.auth.AuthResponse;
import com.yo.yoprj.dto.auth.CurrentUserResponse;
import com.yo.yoprj.dto.auth.LoginRequest;
import com.yo.yoprj.dto.auth.RegisterRequest;
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
}
