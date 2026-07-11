package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findByPromoCode(String promoCode);
}
