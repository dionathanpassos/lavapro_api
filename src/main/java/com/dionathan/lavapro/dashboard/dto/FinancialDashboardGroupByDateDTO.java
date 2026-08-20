package com.dionathan.lavapro.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialDashboardGroupByDateDTO(
        LocalDate date,
        BigDecimal amount
) {


}
