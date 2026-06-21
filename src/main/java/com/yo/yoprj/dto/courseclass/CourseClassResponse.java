package com.yo.yoprj.dto.courseclass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CourseClassResponse(
        Integer id,
        String classCode,
        String name,
        Integer courseId,
        String courseName,
        Integer roomId,
        String roomName,
        Integer scheduleSlotId,
        String scheduleLabel,
        Integer mainTeacherId,
        String mainTeacherName,
        Integer assistantTeacherId,
        String assistantTeacherName,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxStudents,
        BigDecimal tuitionFee,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
