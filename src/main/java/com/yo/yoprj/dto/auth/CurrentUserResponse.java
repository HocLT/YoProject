package com.yo.yoprj.dto.auth;

public record CurrentUserResponse(
        Integer id,
        String username,
        String fullName,
        String role,
        Integer parentId,
        Integer teacherId
) {
}
