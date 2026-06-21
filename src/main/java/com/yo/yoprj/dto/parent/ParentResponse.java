package com.yo.yoprj.dto.parent;

import java.time.LocalDateTime;

public record ParentResponse(
        Integer id,
        String fullName,
        String phone,
        String email,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
