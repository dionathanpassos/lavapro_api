package com.dionathan.lavapro.user.dto;

import com.dionathan.lavapro.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(

        String name,

        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
        String phone,

        @Email(message = "Formato de email inválido")
        String email,

        @Size(min = 8, message = "Tamanho deve ser no mínimo 8 digitos")
        String password,

        Role role
) {
}
