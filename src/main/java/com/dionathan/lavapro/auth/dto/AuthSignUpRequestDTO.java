package com.dionathan.lavapro.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthSignUpRequestDTO(

        @NotBlank(message = "Nome do usuário é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha pé obrigatório")
        @Size(min = 8, message = "Tamanho deve ser no minímo 8 digitos")
        String password,

        @NotBlank(message = "Nome da empresa é obrigatório")
        String companyName,

        @NotBlank(message = "Email da empresa é obrigatório")
        @Email(message = "Formato de email inválido")
        String companyEmail
) {
}
