package com.dionathan.lavapro.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 4, message = "O nome deve conter no mínimo 4 caracteres")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^(\\d{2})?9\\d{8}$", message = "Telefone inválido")
        String phone
) {
}
