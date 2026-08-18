package com.dionathan.lavapro.vehicle.dto;

import com.dionathan.lavapro.customer.dto.CustomerDetailsResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;

import java.time.LocalDateTime;

public record VehicleResponseDTO(
        Long id,
        String plate,
        String model,
        String brand,
        String color,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
