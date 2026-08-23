package com.dionathan.lavapro.payment.dto;

import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentListResponseDTO(
        Long id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime paidAt,
        Long serviceOrderId,
        String customerName,
        String vehiclePlate,
        String vehicleBrand,
        String vehicleModel
) {
}
