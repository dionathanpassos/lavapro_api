package com.dionathan.lavapro.ServiceCatalog.dto;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalogType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServiceCatalogUpdateRequestDTO(

        @Size(min = 5, max = 100, message = "O nome deve ter entre {min} e {max} caracteres")
        String name,

        ServiceCatalogType type,

        @Positive(message = "O valor não pode ser negativo")
        BigDecimal price,

        @Size(max = 500, message = "A descrição deve conter o máximo de {max} caracteres")
        String description
) {
}
