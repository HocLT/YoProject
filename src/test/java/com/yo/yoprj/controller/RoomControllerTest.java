package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.room.RoomResponse;
import com.yo.yoprj.dto.room.RoomUpsertRequest;
import com.yo.yoprj.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@WithMockUser
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService roomService;

    @Test
    void findAll_ReturnsList() throws Exception {
        RoomResponse response = new RoomResponse(1, "R01", "Room 1", 30, "Desc", null, null);
        when(roomService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].roomCode").value("R01"));
    }

    @Test
    void findById_WhenFound_ReturnsRoom() throws Exception {
        RoomResponse response = new RoomResponse(1, "R01", "Room 1", 30, "Desc", null, null);
        when(roomService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/rooms/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomCode").value("R01"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(roomService.findById(1)).thenThrow(new NotFoundException("Room not found"));

        mockMvc.perform(get("/api/rooms/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns201() throws Exception {
        RoomUpsertRequest request = new RoomUpsertRequest("R01", "Room 1", 30, "Desc");
        RoomResponse response = new RoomResponse(1, "R01", "Room 1", 30, "Desc", null, null);

        when(roomService.create(any(RoomUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/rooms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        RoomUpsertRequest request = new RoomUpsertRequest("", "", 0, "Desc");

        mockMvc.perform(post("/api/rooms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        RoomUpsertRequest request = new RoomUpsertRequest("R01", "Room 1", 30, "Desc");
        RoomResponse response = new RoomResponse(1, "R01", "Room 1", 30, "Desc", null, null);

        when(roomService.update(eq(1), any(RoomUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/rooms/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_Returns204() throws Exception {
        doNothing().when(roomService).deleteById(1);

        mockMvc.perform(delete("/api/rooms/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());

        verify(roomService).deleteById(1);
    }
}





