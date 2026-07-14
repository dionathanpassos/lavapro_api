package com.dionathan.lavapro.serviceOrderitem.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ServiceOrderItemRequestDTO(

        @NotNull(message = "O produto é obrigatório")
        Long serviceCatalogId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity

) {
}
