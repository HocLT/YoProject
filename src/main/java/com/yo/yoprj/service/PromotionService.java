package com.yo.yoprj.service;

import com.yo.yoprj.dto.promotion.PromotionResponse;
import com.yo.yoprj.dto.promotion.PromotionUpsertRequest;

import java.util.List;

public interface PromotionService {
    List<PromotionResponse> findAll();
    PromotionResponse findById(Integer id);
    PromotionResponse findByPromoCode(String promoCode);
    PromotionResponse create(PromotionUpsertRequest req);
    PromotionResponse update(Integer id, PromotionUpsertRequest req);
    void deleteById(Integer id);
}
