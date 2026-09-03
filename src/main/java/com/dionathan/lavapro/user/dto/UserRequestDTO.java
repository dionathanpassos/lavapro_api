package com.dionathan.lavapro.user.dto;

import com.dionathan.lavapro.user.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO(
        @NotBlank(message = "Nome do usuário é obrigatório")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^(\\d{2})?9\\d{8}$", message = "Telefone inválido")
        String phone,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotNull(message = "Cargo obrigatório")
        @Enumerated(EnumType.STRING)
        Role role

        ) {
}
