package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.config.AppJwtProperties;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.domain.enums.UserRole;
import com.yo.yoprj.dto.auth.*;
import com.yo.yoprj.repository.UserRepository;
import com.yo.yoprj.service.AuthService;
import com.yo.yoprj.service.TwoFactorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
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
    private final JwtDecoder jwtDecoder;
    private final AppJwtProperties jwtProperties;
    private final TwoFactorAuthService twoFactorAuthService;

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
        user.setIsTwoFactorEnabled(false);

        userRepository.save(user);

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

        if (Boolean.TRUE.equals(user.getIsTwoFactorEnabled())) {
            Instant now = Instant.now();
            Instant expiresAt = now.plusSeconds(5 * 60); // 5 minutes temp token
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer(jwtProperties.issuer())
                    .issuedAt(now)
                    .expiresAt(expiresAt)
                    .subject(user.getUsername())
                    .claim("type", "PRE_AUTH_2FA")
                    .build();

            String tempToken = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
            return new AuthResponse(null, null, null, null, true, tempToken);
        }

        return generateFullAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse verify2FaLogin(Verify2FaRequest request) throws BadRequestException, NotFoundException {
        try {
            Jwt jwt = jwtDecoder.decode(request.tempToken());
            if (!"PRE_AUTH_2FA".equals(jwt.getClaimAsString("type"))) {
                throw new BadCredentialsException("Invalid token type");
            }

            String username = jwt.getSubject();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            if (!Boolean.TRUE.equals(user.getIsTwoFactorEnabled())) {
                throw new BadRequestException("2FA is not enabled for this user");
            }

            if (!twoFactorAuthService.verifyCode(user.getTwoFactorSecret(), request.code())) {
                throw new BadCredentialsException("Invalid 2FA code");
            }

            return generateFullAuthResponse(user);
        } catch (JwtException e) {
            throw new BadCredentialsException("Invalid or expired temp token", e);
        }
    }

    @Transactional
    public TwoFactorSetupResponse generate2FaSetup(String username) throws BadRequestException, NotFoundException {
        User user = findActiveUserByUsername(username);
        if (Boolean.TRUE.equals(user.getIsTwoFactorEnabled())) {
            throw new BadRequestException("2FA is already enabled");
        }

        TwoFactorSetupResponse response = twoFactorAuthService.generateSetup(username);
        user.setTwoFactorSecret(response.secret());
        userRepository.save(user);

        return response;
    }

    @Transactional
    public void enable2Fa(String username, Enable2FaRequest request) throws BadRequestException, NotFoundException {
        User user = findActiveUserByUsername(username);
        if (Boolean.TRUE.equals(user.getIsTwoFactorEnabled())) {
            throw new BadRequestException("2FA is already enabled");
        }
        if (user.getTwoFactorSecret() == null) {
            throw new BadRequestException("2FA setup not initialized");
        }

        if (!twoFactorAuthService.verifyCode(user.getTwoFactorSecret(), request.code())) {
            throw new BadRequestException("Invalid 2FA code");
        }

        user.setIsTwoFactorEnabled(true);
        userRepository.save(user);
    }

    private AuthResponse generateFullAuthResponse(User user) {
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
                toCurrentUserResponse(user),
                false,
                null
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
