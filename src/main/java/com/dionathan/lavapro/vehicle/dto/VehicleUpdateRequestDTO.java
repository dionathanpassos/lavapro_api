package com.dionathan.lavapro.vehicle.dto;

import jakarta.validation.constraints.*;

public record VehicleUpdateRequestDTO(

        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "Placa inválida"
        )
        String plate,

        @Size(min = 2, max = 100)
        String model,

        @Size(min = 2, max = 100)
        String brand,

        @Size(min = 2, max = 50)
        String color,

        @Min(value = 1950, message = "Ano inválido")
        @Max(value = 2100, message = "Ano inválido")
        Integer year,

        Long customerId
) {
}
