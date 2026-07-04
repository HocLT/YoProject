package com.yo.yoprj.dto.learning;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LearningResultCreateRequest {
    @NotNull
    private Integer studentId;

    @NotNull
    private Integer courseClassId;

    @NotNull
    private LocalDate resultMonth;

    @DecimalMin("0.0")
    private BigDecimal score;

    private String teacherComment;
}
