package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.ScheduleSlot;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotResponse;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotUpsertRequest;
import com.yo.yoprj.repository.ScheduleSlotRepository;
import com.yo.yoprj.service.impl.ScheduleSlotServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleSlotServiceTest {

    @Mock
    private ScheduleSlotRepository scheduleSlotRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ScheduleSlotServiceImpl scheduleSlotService;

    @Test
    void findAllReturnsResponses() {
        ScheduleSlot slot = new ScheduleSlot();
        slot.setId(1);
        ScheduleSlotResponse mockResponse = createMockResponse(1);

        when(scheduleSlotRepository.findAll()).thenReturn(List.of(slot));
        when(modelMapper.map(slot, ScheduleSlotResponse.class)).thenReturn(mockResponse);

        List<ScheduleSlotResponse> result = scheduleSlotService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
    }

    @Test
    void findByIdSuccess() {
        ScheduleSlot slot = new ScheduleSlot();
        slot.setId(1);
        ScheduleSlotResponse mockResponse = createMockResponse(1);

        when(scheduleSlotRepository.findById(1)).thenReturn(Optional.of(slot));
        when(modelMapper.map(slot, ScheduleSlotResponse.class)).thenReturn(mockResponse);

        ScheduleSlotResponse result = scheduleSlotService.findById(1);

        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(scheduleSlotRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleSlotService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ScheduleSlot not found: 1");
    }

    @Test
    void createSuccess() {
        ScheduleSlotUpsertRequest request = new ScheduleSlotUpsertRequest("S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc");
        ScheduleSlotResponse mockResponse = createMockResponse(1);
        
        when(scheduleSlotRepository.save(any(ScheduleSlot.class))).thenAnswer(invocation -> {
            ScheduleSlot s = invocation.getArgument(0);
            s.setId(1);
            return s;
        });
        
        doNothing().when(modelMapper).map(any(ScheduleSlotUpsertRequest.class), any(ScheduleSlot.class));
        when(modelMapper.map(any(ScheduleSlot.class), eq(ScheduleSlotResponse.class))).thenReturn(mockResponse);

        ScheduleSlotResponse result = scheduleSlotService.create(request);

        assertThat(result.getId()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(ScheduleSlot.class));
    }

    @Test
    void updateSuccess() {
        ScheduleSlotUpsertRequest request = new ScheduleSlotUpsertRequest("S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc");
        ScheduleSlot slot = new ScheduleSlot();
        slot.setId(1);
        ScheduleSlotResponse mockResponse = createMockResponse(1);

        when(scheduleSlotRepository.findById(1)).thenReturn(Optional.of(slot));
        when(scheduleSlotRepository.save(any(ScheduleSlot.class))).thenReturn(slot);
        doNothing().when(modelMapper).map(any(ScheduleSlotUpsertRequest.class), any(ScheduleSlot.class));
        when(modelMapper.map(any(ScheduleSlot.class), eq(ScheduleSlotResponse.class))).thenReturn(mockResponse);

        ScheduleSlotResponse result = scheduleSlotService.update(1, request);

        assertThat(result.getId()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(ScheduleSlot.class));
    }

    @Test
    void deleteByIdSuccess() {
        doNothing().when(scheduleSlotRepository).deleteById(1);
        scheduleSlotService.deleteById(1);
        verify(scheduleSlotRepository).deleteById(1);
    }

    private ScheduleSlotResponse createMockResponse(Integer id) {
        return new ScheduleSlotResponse(id, "S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc", null, null);
    }
}
