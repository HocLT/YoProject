package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.config.AppJwtProperties;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.domain.enums.UserRole;
import com.yo.yoprj.dto.auth.AuthResponse;
import com.yo.yoprj.dto.auth.CurrentUserResponse;
import com.yo.yoprj.dto.auth.LoginRequest;
import com.yo.yoprj.dto.auth.RegisterRequest;
import com.yo.yoprj.repository.UserRepository;
import com.yo.yoprj.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final AppJwtProperties jwtProperties;

    @Transactional
    public AuthResponse register(RegisterRequest request) throws BadRequestException {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole(UserRole.PARENT);
        user.setIsActive(true);

        userRepository.save(user);

        // Auto login after registration
        return login(new LoginRequest(request.username(), request.password()));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadCredentialsException("User is inactive");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.accessTokenTtlMinutes() * 60);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("roles", List.of(user.getRole().name()))
                .claim("userId", user.getId())
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();

        return new AuthResponse(
                token,
                "Bearer",
                expiresAt,
                toCurrentUserResponse(user)
        );
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse me(String username) throws BadRequestException, NotFoundException {
        return toCurrentUserResponse(findActiveUserByUsername(username));
    }

    @Transactional(readOnly = true)
    public User findActiveUserByUsername(String username) throws BadRequestException, NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadRequestException("User is inactive");
        }
        return user;
    }

    private CurrentUserResponse toCurrentUserResponse(User user) {
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().name(),
                user.getParent() != null ? user.getParent().getId() : null,
                user.getTeacher() != null ? user.getTeacher().getId() : null
        );
    }
}
