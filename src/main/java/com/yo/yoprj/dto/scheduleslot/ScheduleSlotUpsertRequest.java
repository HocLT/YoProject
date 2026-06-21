package com.yo.yoprj.dto.scheduleslot;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSlotUpsertRequest {
    @NotBlank
    @Size(max = 20)
    private String slotCode;

    @NotNull
    @Min(1)
    @Max(7)
    private Byte weekday;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Size(max = 255)
    private String note;
}
