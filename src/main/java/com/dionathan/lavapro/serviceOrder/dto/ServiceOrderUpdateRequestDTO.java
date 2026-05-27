package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.vehicle.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServiceOrderRequestDTO(

        @NotNull(message = "Total é obrigatório")
        BigDecimal totalAmount,

        @NotBlank(message = "Observação é obrigatporia")
        String observations,

        @NotNull(message = "Veículo é obrigatório")
        Long vehicleId
) {
}
