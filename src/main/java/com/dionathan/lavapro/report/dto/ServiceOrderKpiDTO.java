package com.dionathan.lavapro.report.dto;

public record ServiceOrderKpiDTO(
        Long totalServiceOrder,
        Long completed,
        Long canceled,
        Long inProgress
) {
}
