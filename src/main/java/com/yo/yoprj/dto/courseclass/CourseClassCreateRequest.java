package com.yo.yoprj.dto.courseclass;

import com.yo.yoprj.domain.enums.ClassStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CourseClassCreateRequest(
        @NotBlank @Size(max = 20) String classCode,
        @NotBlank @Size(max = 100) String name,
        @NotNull Integer courseId,
        @NotNull Integer roomId,
        @NotNull Integer scheduleSlotId,
        @NotNull Integer mainTeacherId,
        Integer assistantTeacherId,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull @Min(1) Integer maxStudents,
        @NotNull @DecimalMin("0.0") BigDecimal tuitionFee,
        @NotNull ClassStatus status
) {
}
