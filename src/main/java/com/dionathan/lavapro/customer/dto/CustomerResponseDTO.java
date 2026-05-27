package com.dionathan.lavapro.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponseDTO(
        Long id,
        String nome,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
