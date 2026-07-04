package com.yo.yoprj.dto.attendance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceBulkRequest {

    @NotNull
    Integer courseClassId;

    @NotNull
    LocalDate attendanceDate;

    @NotEmpty
    @Valid
    List<StudentAttendanceItem> students;

}
