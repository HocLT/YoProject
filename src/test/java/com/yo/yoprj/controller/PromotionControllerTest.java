package com.yo.yoprj.controller;

import tools.jackson.databind.ObjectMapper;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.promotion.PromotionResponse;
import com.yo.yoprj.dto.promotion.PromotionUpsertRequest;
import com.yo.yoprj.domain.enums.DiscountType;
import com.yo.yoprj.service.PromotionService;
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

@WebMvcTest(PromotionController.class)
@org.springframework.context.annotation.Import({
    com.yo.yoprj.config.SecurityConfig.class,
    org.springframework.boot.autoconfigure.aop.AopAutoConfiguration.class,
    com.yo.yoprj.common.exception.CustomAuthenticationEntryPoint.class,
    com.yo.yoprj.common.exception.CustomAccessDeniedHandler.class
})
@org.springframework.boot.context.properties.EnableConfigurationProperties(com.yo.yoprj.config.AppJwtProperties.class)
class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PromotionService promotionService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_AsAdmin_ReturnsList() throws Exception {
        PromotionResponse response = new PromotionResponse(
                1, "KM10", "Khuyen mai", "PERCENT", BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note", null, null
        );
        when(promotionService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].promoCode").value("KM10"));
    }

    @Test
    @WithMockUser(roles = "PARENT")
    void findAll_AsParent_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ACADEMIC_STAFF")
    void findById_AsAcademicStaff_ReturnsPromotion() throws Exception {
        PromotionResponse response = new PromotionResponse(
                1, "KM10", "Khuyen mai", "PERCENT", BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note", null, null
        );
        when(promotionService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/promotions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(roles = "CASHIER")
    void create_AsCashier_ReturnsCreated() throws Exception {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10", "Khuyen mai", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        PromotionResponse response = new PromotionResponse(
                1, "KM10", "Khuyen mai", "PERCENT", BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note", null, null
        );

        when(promotionService.create(any(PromotionUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/promotions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Promotion created successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ACADEMIC_STAFF")
    void create_AsAcademicStaff_ReturnsForbidden() throws Exception {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10", "Khuyen mai", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );

        mockMvc.perform(post("/api/promotions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_AsAdmin_ReturnsUpdated() throws Exception {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10", "Khuyen mai", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        PromotionResponse response = new PromotionResponse(
                1, "KM10", "Khuyen mai", "PERCENT", BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note", null, null
        );

        when(promotionService.update(eq(1), any(PromotionUpsertRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/promotions/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Promotion updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_AsAdmin_ReturnsSuccess() throws Exception {
        doNothing().when(promotionService).deleteById(1);

        mockMvc.perform(delete("/api/promotions/{id}", 1).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Promotion deleted successfully"));

        verify(promotionService).deleteById(1);
    }
}
