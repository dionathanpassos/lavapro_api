package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.vehicle.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ServiceOrderRequestDTO(

        @NotBlank(message = "Observação é obrigatporia")
        String observations,

        @NotNull(message = "Veículo é obrigatório")
        Long vehicleId
) {
}
