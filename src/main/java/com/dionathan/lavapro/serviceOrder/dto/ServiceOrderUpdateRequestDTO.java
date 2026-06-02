package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServiceOrderUpdateRequestDTO(

        BigDecimal totalAmount,
        String observations,
        ServiceOrderStatus status
) {
}
