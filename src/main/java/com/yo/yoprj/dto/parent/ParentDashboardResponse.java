package com.yo.yoprj.dto.parent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ParentDashboardResponse(
        Integer parentId,
        String parentName,
        String username,
        List<StudentCard> students,
        List<InvoiceCard> invoices,
        List<NotificationCard> notifications
) {
    public record StudentCard(
            Integer id,
            String studentCode,
            String fullName,
            String status,
            BigDecimal latestScore
    ) {}

    public record InvoiceCard(
            Integer id,
            String invoiceCode,
            String studentName,
            String className,
            LocalDate billingMonth,
            BigDecimal finalAmount,
            BigDecimal amountPaid,
            BigDecimal balanceAmount,
            String status,
            LocalDate dueDate
    ) {}

    public record NotificationCard(
            Integer id,
            String type,
            String title,
            String content,
            Boolean isRead,
            LocalDateTime createdAt
    ) {}
}

