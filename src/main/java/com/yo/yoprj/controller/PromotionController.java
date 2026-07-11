package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.dto.promotion.PromotionResponse;
import com.yo.yoprj.dto.promotion.PromotionUpsertRequest;
import com.yo.yoprj.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF', 'CASHIER')")
    public ApiResponse<List<PromotionResponse>> findAll() {
        return ApiResponse.success(promotionService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF', 'CASHIER')")
    public ApiResponse<PromotionResponse> findById(@PathVariable Integer id) {
        return ApiResponse.success(promotionService.findById(id));
    }

    @GetMapping("/code/{promoCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF', 'CASHIER')")
    public ApiResponse<PromotionResponse> findByPromoCode(@PathVariable String promoCode) {
        return ApiResponse.success(promotionService.findByPromoCode(promoCode));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionUpsertRequest req) {
        return ApiResponse.success("Promotion created successfully", promotionService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ApiResponse<PromotionResponse> update(@PathVariable Integer id, @Valid @RequestBody PromotionUpsertRequest req) {
        return ApiResponse.success("Promotion updated successfully", promotionService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ApiResponse<Void> deleteById(@PathVariable Integer id) {
        promotionService.deleteById(id);
        return ApiResponse.successMessage("Promotion deleted successfully");
    }
}
