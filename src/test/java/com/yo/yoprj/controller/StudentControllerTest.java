package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import com.yo.yoprj.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@WithMockUser(roles = "ADMIN") // Need admin for POST /student
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void findAll_ReturnsList() throws Exception {
        StudentResponse response = new StudentResponse(1, "SV12345", "Name", LocalDate.of(2000, 1, 1), "MALE", "10", "School", "0123", 1, "Parent Name", "ACTIVE", BigDecimal.ZERO, "Note", null, null);
        when(studentService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].studentCode").value("SV12345"));
    }

    @Test
    void findById_WhenFound_ReturnsStudent() throws Exception {
        StudentResponse response = new StudentResponse(1, "SV12345", "Name", LocalDate.of(2000, 1, 1), "MALE", "10", "School", "0123", 1, "Parent Name", "ACTIVE", BigDecimal.ZERO, "Note", null, null);
        when(studentService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.studentCode").value("SV12345"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(studentService.findById(1)).thenThrow(new NotFoundException("Student not found"));

        mockMvc.perform(get("/student/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns200() throws Exception {
        StudentUpsertRequest request = new StudentUpsertRequest("SV12345", "Name", LocalDate.of(2000, 1, 1), Gender.MALE, "10", "School", "0123", 1, Status.ACTIVE, BigDecimal.ZERO, "Note");
        StudentResponse response = new StudentResponse(1, "SV12345", "Name", LocalDate.of(2000, 1, 1), "MALE", "10", "School", "0123", 1, "Parent Name", "ACTIVE", BigDecimal.ZERO, "Note", null, null);

        when(studentService.create(any(StudentUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/student").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        StudentUpsertRequest request = new StudentUpsertRequest("123", "", null, null, "", "", "", 0, null, null, "");

        mockMvc.perform(post("/student").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        StudentUpsertRequest request = new StudentUpsertRequest("SV12345", "Name", LocalDate.of(2000, 1, 1), Gender.MALE, "10", "School", "0123", 1, Status.ACTIVE, BigDecimal.ZERO, "Note");
        StudentResponse response = new StudentResponse(1, "SV12345", "Name", LocalDate.of(2000, 1, 1), "MALE", "10", "School", "0123", 1, "Parent Name", "ACTIVE", BigDecimal.ZERO, "Note", null, null);

        when(studentService.update(eq(1), any(StudentUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/student/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
