package com.dionathan.lavapro.vehicle.dto;

import com.dionathan.lavapro.customer.Customer;
import jakarta.validation.constraints.*;

public record VehicleRequestDTO(

        @NotBlank(message = "Placa é obrigatória")
        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "Placa inválida"
        )
        String plate,

        @NotBlank(message = "Modelo é obrigatório")
        @Size(min = 2, max = 100, message = "Deve conter no minimo 2 caracteres")
        String model,

        @NotBlank(message = "Marca é obrigatória")
        @Size(min = 2, max = 100)
        String brand,

        @Size(min = 2, max = 50, message = "Cor é obrigatório")
        String color,

        @Min(value = 1800, message = "Ano inválido")
        @Max(value = 2100, message = "Ano inválido")
        Integer year,

        @NotNull(message = "Cliente é obrigatório")
        Long customerId
) {
}
