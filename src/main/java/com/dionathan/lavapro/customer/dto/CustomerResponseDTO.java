package com.dionathan.lavapro.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponseDTO(
        Long id,
        String name,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
