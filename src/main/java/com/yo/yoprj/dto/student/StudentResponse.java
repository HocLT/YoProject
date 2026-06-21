package com.yo.yoprj.dto.student;

import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudentResponse(
        Integer id,
        String studentCode,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String gradeLevel,
        String schoolName,
        String phone,
        Integer parentId,
        String parentName,
        String status,
        BigDecimal latestScore,
        String note,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

