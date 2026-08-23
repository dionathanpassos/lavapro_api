package com.dionathan.lavapro.payment.dto;

import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentIndicatorsDTO(
        Long total,
        Long totalPaid,
        Long totalCanceled


) {
}
