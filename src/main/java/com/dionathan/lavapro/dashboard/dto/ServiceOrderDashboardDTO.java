package com.dionathan.lavapro.dashboard.dto;

public record ServiceOrderDashboardDTO(
        Long waiting,
        Long inProgress,
        Long ready,
        Long delivered,
        Long canceled,
        Long totalMonth

) {
}
