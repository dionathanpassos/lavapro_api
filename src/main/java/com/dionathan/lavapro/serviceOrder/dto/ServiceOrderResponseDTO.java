package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceOrderResponseDTO(
        Long id,
        BigDecimal totalAmount,
        String observations,
        ServiceOrderStatus status,
        VehicleSummaryResponseDTO vehicle,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
