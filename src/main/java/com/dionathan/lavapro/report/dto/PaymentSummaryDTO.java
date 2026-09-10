package com.dionathan.lavapro.report.dto;

import com.dionathan.lavapro.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentSummaryDTO(
        PaymentMethod paymentMethod,
        Long quantity,
        BigDecimal totalAmount
) {}
