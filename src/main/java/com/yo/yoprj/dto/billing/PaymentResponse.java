package com.yo.yoprj.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Integer id;
    private Integer invoiceId;
    private String invoiceCode;
    private String paymentCode;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private Integer cashierUserId;
    private String cashierUsername;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
