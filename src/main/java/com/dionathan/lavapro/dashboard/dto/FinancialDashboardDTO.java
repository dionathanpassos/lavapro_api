package com.dionathan.lavapro.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record FinancialDashboardDTO(
        BigDecimal monthRevenue,
        BigDecimal todayRevenue,
        BigDecimal averageTicket,
        Long countRefunded,
        Long countPaid,
        List<FinancialDashboardGroupByDateDTO> revenueByDate
) {
}
