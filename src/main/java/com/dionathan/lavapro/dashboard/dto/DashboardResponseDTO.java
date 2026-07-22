package com.dionathan.lavapro.dashboard.dto;

public record DashboardResponseDTO(
        ServiceOrderDashboardDTO serviceOrders,
        CustomerDashboardDTO customers,
        FinancialDashboardDTO financial,
        CashFlowDashboardDTO cashFlow,
        BestSellingServiceDTO service
) {
}
