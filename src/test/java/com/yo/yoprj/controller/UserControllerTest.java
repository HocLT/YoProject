package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.enums.UserRole;
import com.yo.yoprj.dto.user.UserResponse;
import com.yo.yoprj.dto.user.UserUpsertRequest;
import com.yo.yoprj.service.UserService;
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

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void findAll_ReturnsList() throws Exception {
        UserResponse response = new UserResponse(1, "user", "Name", "0123", "email", "PARENT", 2, null, true, null, null);
        when(userService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user"));
    }

    @Test
    void findById_WhenFound_ReturnsUser() throws Exception {
        UserResponse response = new UserResponse(1, "user", "Name", "0123", "email", "PARENT", 2, null, true, null, null);
        when(userService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    void findById_WhenNotFound_Returns404() throws Exception {
        when(userService.findById(1)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidData_Returns201() throws Exception {
        UserUpsertRequest request = new UserUpsertRequest("user", "hash", "Name", "0123", "email", UserRole.PARENT, 2, null, true);
        UserResponse response = new UserResponse(1, "user", "Name", "0123", "email", "PARENT", 2, null, true, null, null);

        when(userService.create(any(UserUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_WithInvalidData_Returns400() throws Exception {
        UserUpsertRequest request = new UserUpsertRequest("", "", "Name", "0123", "email", null, 2, null, true);

        mockMvc.perform(post("/api/users").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WithValidData_Returns200() throws Exception {
        UserUpsertRequest request = new UserUpsertRequest("user", "hash", "Name", "0123", "email", UserRole.PARENT, 2, null, true);
        UserResponse response = new UserResponse(1, "user", "Name", "0123", "email", "PARENT", 2, null, true, null, null);

        when(userService.update(eq(1), any(UserUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_Returns204() throws Exception {
        doNothing().when(userService).deleteById(1);

        mockMvc.perform(delete("/api/users/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1);
    }
}





