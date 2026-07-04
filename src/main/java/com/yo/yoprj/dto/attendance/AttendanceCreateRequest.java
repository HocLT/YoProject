package com.yo.yoprj.dto.attendance;

import com.yo.yoprj.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCreateRequest {
    @NotNull
    private Integer courseClassId;
    @NotNull
    private Integer studentId;
    @NotNull
    private LocalDate attendanceDate;
    @NotNull
    private AttendanceStatus status;
    @Size(max = 255)
    private String note;
}
