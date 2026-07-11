package com.yo.yoprj.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "InvoiceResponse", description = "Invoice details returned by billing endpoints.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    @Schema(description = "Invoice identifier.", example = "1")
    private Integer id;
    @Schema(description = "Invoice code.", example = "INV-2026-001")
    String invoiceCode;
    @Schema(description = "Student identifier.", example = "1")
    private Integer studentId;
    @Schema(description = "Student full name.", example = "Nguyen Van A")
    private String studentName;
    @Schema(description = "Course class identifier.", example = "3")
    private Integer courseClassId;
    @Schema(description = "Course class name.", example = "English Beginner - Morning")
    private String className;
    @Schema(description = "Billing month.", example = "2026-06-01")
    private LocalDate billingMonth;
    @Schema(description = "Original amount before discount.", example = "2800000")
    private BigDecimal originalAmount;
    @Schema(description = "Discount amount applied.", example = "300000")
    private BigDecimal discountAmount;
    @Schema(description = "Final amount after discount.", example = "2500000")
    private BigDecimal finalAmount;
    @Schema(description = "Amount already paid.", example = "1000000")
    private BigDecimal amountPaid;
    @Schema(description = "Remaining balance.", example = "1500000")
    private BigDecimal balanceAmount;
    @Schema(description = "Invoice status.", example = "PARTIALLY_PAID")
    private String status;
    @Schema(description = "Promotion identifier.", example = "2", nullable = true)
    private Integer promotionId;
    @Schema(description = "Promotion name.", example = "Summer discount", nullable = true)
    private String promotionName;
    @Schema(description = "Due date.", example = "2026-06-15", nullable = true)
    private LocalDate dueDate;
    @Schema(description = "Billing note.", example = "Early bird discount applied.", nullable = true)
    private String note;
    @Schema(description = "Creation timestamp.", example = "2026-05-24T10:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp.", example = "2026-05-24T11:30:00")
    private LocalDateTime updatedAt;
}

