package com.yo.yoprj.dto.enrollment;

import com.yo.yoprj.domain.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnrollmentUpdateRequest {
    @NotNull
    private EnrollmentStatus status;

    @Size(max = 255)
    private String note;
}
