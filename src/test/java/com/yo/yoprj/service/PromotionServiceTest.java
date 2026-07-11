package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.ConflictException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Promotion;
import com.yo.yoprj.domain.enums.DiscountType;
import com.yo.yoprj.dto.promotion.PromotionResponse;
import com.yo.yoprj.dto.promotion.PromotionUpsertRequest;
import com.yo.yoprj.repository.PromotionRepository;
import com.yo.yoprj.service.impl.PromotionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    @Test
    void findAllReturnsResponses() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        PromotionResponse mockResponse = createMockResponse(1, "KM10");

        when(promotionRepository.findAll()).thenReturn(List.of(promotion));
        when(modelMapper.map(promotion, PromotionResponse.class)).thenReturn(mockResponse);

        List<PromotionResponse> result = promotionService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getPromoCode()).isEqualTo("KM10");
    }

    @Test
    void findByIdSuccess() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        PromotionResponse mockResponse = createMockResponse(1, "KM10");

        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(modelMapper.map(promotion, PromotionResponse.class)).thenReturn(mockResponse);

        PromotionResponse result = promotionService.findById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getPromoCode()).isEqualTo("KM10");
    }

    @Test
    void findByIdThrowsNotFound() {
        when(promotionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Promotion not found with id: 1");
    }

    @Test
    void findByPromoCodeSuccess() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        PromotionResponse mockResponse = createMockResponse(1, "KM10");

        when(promotionRepository.findByPromoCode("KM10")).thenReturn(Optional.of(promotion));
        when(modelMapper.map(promotion, PromotionResponse.class)).thenReturn(mockResponse);

        PromotionResponse result = promotionService.findByPromoCode("KM10");

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getPromoCode()).isEqualTo("KM10");
    }

    @Test
    void findByPromoCodeThrowsNotFound() {
        when(promotionRepository.findByPromoCode("KM10")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.findByPromoCode("KM10"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Promotion not found with code: KM10");
    }

    @Test
    void createSuccess() {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10", "Khuyen mai 10%", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        PromotionResponse mockResponse = createMockResponse(1, "KM10");

        when(promotionRepository.findByPromoCode("KM10")).thenReturn(Optional.empty());
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(invocation -> {
            Promotion p = invocation.getArgument(0);
            p.setId(1);
            return p;
        });
        doNothing().when(modelMapper).map(any(PromotionUpsertRequest.class), any(Promotion.class));
        when(modelMapper.map(any(Promotion.class), eq(PromotionResponse.class))).thenReturn(mockResponse);

        PromotionResponse result = promotionService.create(request);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getPromoCode()).isEqualTo("KM10");
        verify(promotionRepository).save(any(Promotion.class));
    }

    @Test
    void createThrowsConflictWhenPromoCodeExists() {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10", "Khuyen mai 10%", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        Promotion existing = new Promotion();
        existing.setId(2);
        existing.setPromoCode("KM10");

        when(promotionRepository.findByPromoCode("KM10")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> promotionService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Promo code already exists: KM10");
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    void updateSuccess() {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM10-NEW", "Khuyen mai 10%", DiscountType.PERCENT, BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setPromoCode("KM10");
        PromotionResponse mockResponse = createMockResponse(1, "KM10-NEW");

        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(promotionRepository.findByPromoCode("KM10-NEW")).thenReturn(Optional.empty());
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        doNothing().when(modelMapper).map(any(PromotionUpsertRequest.class), any(Promotion.class));
        when(modelMapper.map(any(Promotion.class), eq(PromotionResponse.class))).thenReturn(mockResponse);

        PromotionResponse result = promotionService.update(1, request);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getPromoCode()).isEqualTo("KM10-NEW");
    }

    @Test
    void updateThrowsConflictWhenPromoCodeExistsOnAnotherPromotion() {
        PromotionUpsertRequest request = new PromotionUpsertRequest(
                "KM20", "Khuyen mai 20%", DiscountType.PERCENT, BigDecimal.valueOf(20),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note"
        );
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setPromoCode("KM10");

        Promotion another = new Promotion();
        another.setId(2);
        another.setPromoCode("KM20");

        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(promotionRepository.findByPromoCode("KM20")).thenReturn(Optional.of(another));

        assertThatThrownBy(() -> promotionService.update(1, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Promo code already exists: KM20");
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    void deleteByIdSuccess() {
        when(promotionRepository.existsById(1)).thenReturn(true);
        doNothing().when(promotionRepository).deleteById(1);

        promotionService.deleteById(1);

        verify(promotionRepository).deleteById(1);
    }

    @Test
    void deleteByIdThrowsNotFound() {
        when(promotionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> promotionService.deleteById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Promotion not found with id: 1");
        verify(promotionRepository, never()).deleteById(1);
    }

    private PromotionResponse createMockResponse(Integer id, String promoCode) {
        return new PromotionResponse(
                id, promoCode, "Khuyen mai", "PERCENT", BigDecimal.valueOf(10),
                LocalDate.now(), LocalDate.now().plusDays(10), true, "Note", null, null
        );
    }
}
