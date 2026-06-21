package com.yo.yoprj.dto.scheduleslot;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleSlotResponse(
        Integer id,
        String slotCode,
        Byte weekday,
        LocalTime startTime,
        LocalTime endTime,
        String note,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
