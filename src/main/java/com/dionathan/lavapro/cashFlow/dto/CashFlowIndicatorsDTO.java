package com.dionathan.lavapro.cashFlow.dto;

import java.math.BigDecimal;

public record CashFlowIndicatorsDTO(
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance,
        BigDecimal balanceTotal



) {
}
