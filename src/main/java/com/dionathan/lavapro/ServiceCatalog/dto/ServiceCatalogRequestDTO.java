package com.dionathan.lavapro.ServiceCatalog.dto;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalogType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ServiceCatalogRequestDTO(

        @NotBlank
        @Size(min = 5, max = 100, message = "O nome deve ter entre {min} e {max} caracteres")
        String name,

        @NotNull(message = "Tipo é obrigatório")
        ServiceCatalogType type,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço mínimo é R$ 0,01")
        BigDecimal price,

        @Size(max = 500, message = "A descrição deve conter o máximo de {max} caracteres")
        String description
) {
}
