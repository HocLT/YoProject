package com.yo.yoprj.dto.billing;


import com.yo.yoprj.domain.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentCreateRequest {
    @NotNull
    private Integer invoiceId;

    @NotBlank
    private String paymentCode;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal paidAmount;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private LocalDateTime paidAt;

    @Size(max = 255)
    private String note;
}
