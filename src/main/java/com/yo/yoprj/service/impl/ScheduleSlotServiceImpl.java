package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.ScheduleSlot;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotResponse;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotUpsertRequest;
import com.yo.yoprj.repository.ScheduleSlotRepository;
import com.yo.yoprj.service.ScheduleSlotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleSlotServiceImpl implements ScheduleSlotService {

    private final ScheduleSlotRepository scheduleSlotRepository;
    private final ModelMapper modelMapper;

    private ScheduleSlotResponse map(ScheduleSlot scheduleSlot) {
        return modelMapper.map(scheduleSlot, ScheduleSlotResponse.class);
    }

    private ScheduleSlot toScheduleSlot(ScheduleSlotUpsertRequest req) {
        ScheduleSlot scheduleSlot = new ScheduleSlot();
        modelMapper.map(req, scheduleSlot);
        return scheduleSlot;
    }

    @Override
    public List<ScheduleSlotResponse> findAll() {
        return scheduleSlotRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ScheduleSlotResponse findById(Integer id) {
        return scheduleSlotRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("ScheduleSlot not found: " + id));
    }

    @Override
    public ScheduleSlotResponse create(ScheduleSlotUpsertRequest req) {
        ScheduleSlot scheduleSlot = toScheduleSlot(req);
        scheduleSlot = scheduleSlotRepository.save(scheduleSlot);
        return map(scheduleSlot);
    }

    @Override
    public ScheduleSlotResponse update(Integer id, ScheduleSlotUpsertRequest req) {
        ScheduleSlot scheduleSlot = scheduleSlotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ScheduleSlot not found: " + id));
        modelMapper.map(req, scheduleSlot);
        scheduleSlot = scheduleSlotRepository.save(scheduleSlot);
        return map(scheduleSlot);
    }

    @Override
    public void deleteById(Integer id) {
        scheduleSlotRepository.deleteById(id);
    }
}
