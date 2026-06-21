package com.yo.yoprj.controller;

import com.yo.yoprj.dto.scheduleslot.ScheduleSlotResponse;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotUpsertRequest;
import com.yo.yoprj.service.ScheduleSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-slots")
@RequiredArgsConstructor
public class ScheduleSlotController {

    private final ScheduleSlotService scheduleSlotService;

    @GetMapping
    public List<ScheduleSlotResponse> findAll() {
        return scheduleSlotService.findAll();
    }

    @GetMapping("/{id}")
    public ScheduleSlotResponse findById(@PathVariable Integer id) {
        return scheduleSlotService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleSlotResponse create(@RequestBody @Valid ScheduleSlotUpsertRequest request) {
        return scheduleSlotService.create(request);
    }

    @PutMapping("/{id}")
    public ScheduleSlotResponse update(@PathVariable Integer id, @RequestBody @Valid ScheduleSlotUpsertRequest request) {
        return scheduleSlotService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        scheduleSlotService.deleteById(id);
    }
}
