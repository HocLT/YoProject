package com.yo.yoprj.dto.room;

import java.time.LocalDateTime;

public record RoomResponse(
        Integer id,
        String roomCode,
        String name,
        Integer capacity,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
