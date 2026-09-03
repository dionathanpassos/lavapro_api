package com.dionathan.lavapro.user.dto;

public record UserIndicatorsDTO(
        Long totalUsers,
        Long totalActive,
        Long totalInactive
) {
}
