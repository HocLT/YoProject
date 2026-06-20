package com.yo.yoprj.dto.student;

import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentResponse {
    private int id;
    private String studentCode;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String gradeLevel;
    private String schoolName;
    private String phone;
    private int parentId;
    private Status status;
    private BigDecimal latestScore;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
