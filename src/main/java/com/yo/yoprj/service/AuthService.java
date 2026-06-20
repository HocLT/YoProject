package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.dto.auth.AuthResponse;
import com.yo.yoprj.dto.auth.CurrentUserResponse;
import com.yo.yoprj.dto.auth.LoginRequest;
import com.yo.yoprj.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request) throws BadRequestException;

    AuthResponse login(LoginRequest request);

    CurrentUserResponse me(String username) throws BadRequestException, NotFoundException;

    User findActiveUserByUsername(String username) throws BadRequestException, NotFoundException;
}
