package com.dionathan.lavapro.serviceOrderitem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ServiceOrderItemUpdateRequestDTO(

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity
) {
}
