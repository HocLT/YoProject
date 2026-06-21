package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.enums.TeacherRole;
import com.yo.yoprj.dto.teacher.TeacherResponse;
import com.yo.yoprj.dto.teacher.TeacherUpsertRequest;
import com.yo.yoprj.service.TeacherService;
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

@WebMvcTest(TeacherController.class)
@WithMockUser
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeacherService teacherService;

    @Test
    void findAll_ReturnsList() throws Exception {
        TeacherResponse response = new TeacherResponse(1, "T01", "Name", "0123", "email", "TEACHER", "url", true, null, null);
        when(teacherService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].teacherCode").value("T01"));
    }

    @Test
    void findById_WhenFound_ReturnsTeacher() throws Exception {
        TeacherResponse response = new TeacherResponse(1, "T01", "Name", "0123", "email", "TEACHER", "url", true, null, null);
        when(teacherService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teacherCode").value("T01"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(teacherService.findById(1)).thenThrow(new NotFoundException("Teacher not found"));

        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns201() throws Exception {
        TeacherUpsertRequest request = new TeacherUpsertRequest("T01", "Name", "0123", "email", TeacherRole.TEACHER, "url", true);
        TeacherResponse response = new TeacherResponse(1, "T01", "Name", "0123", "email", "TEACHER", "url", true, null, null);

        when(teacherService.create(any(TeacherUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/teachers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        TeacherUpsertRequest request = new TeacherUpsertRequest("", "", "0123", "email", null, "url", true);

        mockMvc.perform(post("/api/teachers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        TeacherUpsertRequest request = new TeacherUpsertRequest("T01", "Name", "0123", "email", TeacherRole.TEACHER, "url", true);
        TeacherResponse response = new TeacherResponse(1, "T01", "Name", "0123", "email", "TEACHER", "url", true, null, null);

        when(teacherService.update(eq(1), any(TeacherUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/teachers/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_Returns204() throws Exception {
        doNothing().when(teacherService).deleteById(1);

        mockMvc.perform(delete("/api/teachers/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());

        verify(teacherService).deleteById(1);
    }
}





