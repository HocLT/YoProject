package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotResponse;
import com.yo.yoprj.dto.scheduleslot.ScheduleSlotUpsertRequest;
import com.yo.yoprj.service.ScheduleSlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleSlotController.class)
@WithMockUser
class ScheduleSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScheduleSlotService scheduleSlotService;

    @Test
    void findAll_ReturnsList() throws Exception {
        ScheduleSlotResponse response = new ScheduleSlotResponse(1, "S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc", null, null);
        when(scheduleSlotService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/schedule-slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].slotCode").value("S01"));
    }

    @Test
    void findById_WhenFound_ReturnsSlot() throws Exception {
        ScheduleSlotResponse response = new ScheduleSlotResponse(1, "S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc", null, null);
        when(scheduleSlotService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/schedule-slots/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.slotCode").value("S01"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(scheduleSlotService.findById(1)).thenThrow(new NotFoundException("Slot not found"));

        mockMvc.perform(get("/api/schedule-slots/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns201() throws Exception {
        ScheduleSlotUpsertRequest request = new ScheduleSlotUpsertRequest("S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc");
        ScheduleSlotResponse response = new ScheduleSlotResponse(1, "S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc", null, null);

        when(scheduleSlotService.create(any(ScheduleSlotUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/schedule-slots").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        ScheduleSlotUpsertRequest request = new ScheduleSlotUpsertRequest("", (byte)8, null, null, "Desc");

        mockMvc.perform(post("/api/schedule-slots").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        ScheduleSlotUpsertRequest request = new ScheduleSlotUpsertRequest("S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc");
        ScheduleSlotResponse response = new ScheduleSlotResponse(1, "S01", (byte)1, LocalTime.of(8,0), LocalTime.of(10,0), "Desc", null, null);

        when(scheduleSlotService.update(eq(1), any(ScheduleSlotUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/schedule-slots/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_Returns204() throws Exception {
        doNothing().when(scheduleSlotService).deleteById(1);

        mockMvc.perform(delete("/api/schedule-slots/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());

        verify(scheduleSlotService).deleteById(1);
    }
}





