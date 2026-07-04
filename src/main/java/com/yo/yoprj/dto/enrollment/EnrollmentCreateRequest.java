package com.yo.yoprj.dto.enrollment;

import com.yo.yoprj.domain.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentCreateRequest {
    @NotNull
    private Integer studentId;

    @NotNull
    private Integer courseClassId;

    @NotNull
    private LocalDate enrolledAt;

    @NotNull
    private EnrollmentStatus status;

    @Size(max = 255)
    private String note;
}
