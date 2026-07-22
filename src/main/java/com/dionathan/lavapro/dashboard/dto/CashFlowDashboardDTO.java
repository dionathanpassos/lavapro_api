package com.dionathan.lavapro.dashboard.dto;

import java.math.BigDecimal;

public record CashFlowDashboardDTO(
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance
) {
}
