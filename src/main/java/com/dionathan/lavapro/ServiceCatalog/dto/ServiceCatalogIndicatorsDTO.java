package com.dionathan.lavapro.ServiceCatalog.dto;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalogType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceCatalogIndicatorsDTO(
        Long totalProducts,
        Long totalActive
) {
}
