package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.ConflictException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Promotion;
import com.yo.yoprj.dto.promotion.PromotionResponse;
import com.yo.yoprj.dto.promotion.PromotionUpsertRequest;
import com.yo.yoprj.repository.PromotionRepository;
import com.yo.yoprj.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;

    private PromotionResponse map(Promotion promotion) {
        return modelMapper.map(promotion, PromotionResponse.class);
    }

    private Promotion toPromotion(PromotionUpsertRequest req) {
        Promotion promotion = new Promotion();
        modelMapper.map(req, promotion);
        return promotion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> findAll() {
        return promotionRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse findById(Integer id) {
        return promotionRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse findByPromoCode(String promoCode) {
        return promotionRepository.findByPromoCode(promoCode)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("Promotion not found with code: " + promoCode));
    }

    @Override
    @Transactional
    public PromotionResponse create(PromotionUpsertRequest req) {
        if (promotionRepository.findByPromoCode(req.getPromoCode()).isPresent()) {
            throw new ConflictException("Promo code already exists: " + req.getPromoCode());
        }
        Promotion promotion = toPromotion(req);
        promotion = promotionRepository.save(promotion);
        return map(promotion);
    }

    @Override
    @Transactional
    public PromotionResponse update(Integer id, PromotionUpsertRequest req) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));

        promotionRepository.findByPromoCode(req.getPromoCode())
                .ifPresent(existing -> {
                    if (existing.getId() != id) {
                        throw new ConflictException("Promo code already exists: " + req.getPromoCode());
                    }
                });

        modelMapper.map(req, promotion);
        promotion = promotionRepository.save(promotion);
        return map(promotion);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!promotionRepository.existsById(id)) {
            throw new NotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }
}
