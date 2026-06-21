package com.yo.yoprj.dto.teacher;

import java.time.LocalDateTime;

public record TeacherResponse(
        Integer id,
        String teacherCode,
        String fullName,
        String phone,
        String email,
        String teacherRole,
        String cccdImageUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
