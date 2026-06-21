package com.yo.yoprj.domain.entity;

import com.yo.yoprj.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "schedule_slots")
public class ScheduleSlot extends AuditableEntity {

    @Column(name = "slot_code", nullable = false, unique = true, length = 20)
    private String slotCode;

    @Column(nullable = false)
    private Byte weekday;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(length = 255)
    private String note;
}

