package com.yo.yoprj.dto.auth;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        CurrentUserResponse user
) {
}
