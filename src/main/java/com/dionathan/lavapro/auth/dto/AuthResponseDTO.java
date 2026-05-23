package com.dionathan.lavapro.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponseDTO(
        String token
) {
}
