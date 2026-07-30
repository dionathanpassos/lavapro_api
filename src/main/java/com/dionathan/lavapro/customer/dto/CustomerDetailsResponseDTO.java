package com.dionathan.lavapro.customer.dto;

import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerDetailsResponseDTO(
        Long id,
        String nome,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        List<VehicleResponseDTO> vehicles
) {
}
