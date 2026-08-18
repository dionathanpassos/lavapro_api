package com.dionathan.lavapro.dashboard.dto;

import java.util.List;

public record DashboardResponseDTO(
        ServiceOrderDashboardDTO serviceOrders,
        CustomerDashboardDTO customers,
        FinancialDashboardDTO financial,
        CashFlowDashboardDTO cashFlow,
        List<BestSellingServiceDTO> services
) {
}
