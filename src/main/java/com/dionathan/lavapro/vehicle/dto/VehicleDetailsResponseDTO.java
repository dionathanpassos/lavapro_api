package com.dionathan.lavapro.vehicle.dto;

import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;

import java.time.LocalDateTime;

public record VehicleDetailsResponseDTO(
        Long id,
        String plate,
        String model,
        String brand,
        String color,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        CustomerSummaryResponseDTO customer

) {
}
