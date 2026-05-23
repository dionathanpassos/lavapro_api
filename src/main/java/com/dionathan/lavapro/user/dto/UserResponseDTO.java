package com.dionathan.lavapro.user.dto;

import com.dionathan.lavapro.user.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
