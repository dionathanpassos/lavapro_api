package com.dionathan.lavapro.payment.dto;

import com.dionathan.lavapro.payment.PaymentMethod;

public record PaymentRequestDTO(
        PaymentMethod paymentMethod,
        Long serviceOrderId
) {
}
