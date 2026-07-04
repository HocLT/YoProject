package com.yo.yoprj.dto.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningResultResponse {
    private Integer id;
    private Integer studentId;
    private String studentName;
    private Integer courseClassId;
    private String className;
    private LocalDate resultMonth;
    private BigDecimal score;
    private String teacherComment;
    private Integer createdByUserId;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

