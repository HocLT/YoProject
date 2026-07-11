package com.yo.yoprj.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "InvoiceCreateRequest", description = "Payload used to create a tuition invoice.")
@Data
public class InvoiceCreateRequest {
    @Schema(description = "Unique invoice code.", example = "INV-2026-001")
    @NotNull
    private String invoiceCode;

    @Schema(description = "Student identifier receiving the invoice.", example = "1")
    @NotNull
    private Integer studentId;

    @Schema(description = "Course class identifier billed.", example = "3")
    @NotNull
    private Integer courseClassId;

    @Schema(description = "Billing month represented by a date in that month.", example = "2026-06-01")
    @NotNull
    private LocalDate billingMonth;

    @Schema(description = "Original amount before discount.", example = "2800000", nullable = true)
    @DecimalMin("0.0")
    private BigDecimal originalAmount;

    @Schema(description = "Optional promotion identifier.", example = "2", nullable = true)
    private Integer promotionId;

    @Schema(description = "Payment due date.", example = "2026-06-15", nullable = true)
    private LocalDate dueDate;

    @Schema(description = "Extra billing note.", example = "Early bird discount applied.", nullable = true)
    @Size(max = 255)
    private String note;
}
