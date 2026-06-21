package com.yo.yoprj.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String username,
        String fullName,
        String phone,
        String email,
        String role,
        Integer parentId,
        Integer teacherId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
