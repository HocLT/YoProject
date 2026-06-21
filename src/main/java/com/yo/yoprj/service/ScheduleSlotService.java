package com.yo.yoprj.service;

import com.yo.yoprj.dto.scheduleslot.ScheduleSlotResponse;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotUpsertRequest;

import java.util.List;

public interface ScheduleSlotService {
    List<ScheduleSlotResponse> findAll();
    ScheduleSlotResponse findById(Integer id);
    ScheduleSlotResponse create(ScheduleSlotUpsertRequest req);
    ScheduleSlotResponse update(Integer id, ScheduleSlotUpsertRequest req);
    void deleteById(Integer id);
}
