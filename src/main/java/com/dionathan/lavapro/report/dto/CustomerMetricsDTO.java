package com.dionathan.lavapro.report.dto;

import java.math.BigDecimal;

public record CustomerMetricsDTO(
        Long customerId,
        String customerName,
        String customerPhone,
        Long totalOrders,
        BigDecimal amountPayment
) {
}
