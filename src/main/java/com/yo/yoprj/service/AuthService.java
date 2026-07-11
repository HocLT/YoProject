package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.dto.auth.AuthResponse;
import com.yo.yoprj.dto.auth.CurrentUserResponse;
import com.yo.yoprj.dto.auth.LoginRequest;
import com.yo.yoprj.dto.auth.RegisterRequest;

import com.yo.yoprj.dto.auth.TwoFactorSetupResponse;
import com.yo.yoprj.dto.auth.Verify2FaRequest;
import com.yo.yoprj.dto.auth.Enable2FaRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request) throws BadRequestException;

    AuthResponse login(LoginRequest request);

    CurrentUserResponse me(String username) throws BadRequestException, NotFoundException;

    User findActiveUserByUsername(String username) throws BadRequestException, NotFoundException;

    AuthResponse verify2FaLogin(Verify2FaRequest request) throws BadRequestException, NotFoundException;

    TwoFactorSetupResponse generate2FaSetup(String username) throws BadRequestException, NotFoundException;

    void enable2Fa(String username, Enable2FaRequest request) throws BadRequestException, NotFoundException;
}
