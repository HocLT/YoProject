package com.yo.yoprj.dto.promotion;

import com.yo.yoprj.domain.enums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionUpsertRequest {
    @NotBlank
    @Size(max = 30)
    private String promoCode;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discountValue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Boolean isActive = true;

    @Size(max = 255)
    private String note;
}
