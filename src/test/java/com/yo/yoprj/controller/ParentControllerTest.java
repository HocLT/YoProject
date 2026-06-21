package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.parent.ParentResponse;
import com.yo.yoprj.dto.parent.ParentUpsertRequest;
import com.yo.yoprj.service.ParentService;
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

@WebMvcTest(ParentController.class)
@WithMockUser
class ParentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ParentService parentService;

    @Test
    void findAll_ReturnsList() throws Exception {
        ParentResponse response = new ParentResponse(1, "Name", "0123", "email", "Address", null, null);
        when(parentService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/parents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Name"));
    }

    @Test
    void findById_WhenFound_ReturnsParent() throws Exception {
        ParentResponse response = new ParentResponse(1, "Name", "0123", "email", "Address", null, null);
        when(parentService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/parents/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Name"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(parentService.findById(1)).thenThrow(new NotFoundException("Parent not found"));

        mockMvc.perform(get("/api/parents/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns201() throws Exception {
        ParentUpsertRequest request = new ParentUpsertRequest("Name", "0123", "email", "Address");
        ParentResponse response = new ParentResponse(1, "Name", "0123", "email", "Address", null, null);

        when(parentService.create(any(ParentUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/parents").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        ParentUpsertRequest request = new ParentUpsertRequest("", "", "email", "Address");

        mockMvc.perform(post("/api/parents").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        ParentUpsertRequest request = new ParentUpsertRequest("Name", "0123", "email", "Address");
        ParentResponse response = new ParentResponse(1, "Name", "0123", "email", "Address", null, null);

        when(parentService.update(eq(1), any(ParentUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/parents/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_Returns204() throws Exception {
        doNothing().when(parentService).deleteById(1);

        mockMvc.perform(delete("/api/parents/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());

        verify(parentService).deleteById(1);
    }
}





