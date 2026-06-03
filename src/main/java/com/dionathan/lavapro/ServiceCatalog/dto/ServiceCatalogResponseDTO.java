package com.dionathan.lavapro.ServiceCatalog.dto;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalogType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceCatalogResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        ServiceCatalogType type,
        boolean active,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
