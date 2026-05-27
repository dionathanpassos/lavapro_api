package com.dionathan.lavapro.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(

        @NotBlank
        @Size(min = 4, message = "O nome deve conter no mínimo 4 caracteres")
        String name,

        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
        String phone
) {
}
