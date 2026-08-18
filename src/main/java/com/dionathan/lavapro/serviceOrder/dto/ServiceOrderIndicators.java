package com.dionathan.lavapro.serviceOrder.dto;

public record ServiceOrderIndicators(
        Long waiting,
        Long inProgress,
        Long ready,
        Long delivered,
        Long canceled
) {
}
