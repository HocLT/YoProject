package com.yo.yoprj.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Integer id;
    private Integer courseClassId;
    private String className;
    private Integer studentId;
    private String studentName;
    private LocalDate attendanceDate;
    private String status;
    private String note;
    private Integer recordedByUserId;
    private String recordedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
