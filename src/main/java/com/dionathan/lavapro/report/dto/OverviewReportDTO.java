package com.dionathan.lavapro.report.dto;

import com.dionathan.lavapro.dashboard.dto.BestSellingServiceDTO;
import com.dionathan.lavapro.dashboard.dto.FinancialDashboardGroupByDateDTO;
import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record OverviewReportDTO(
        BigDecimal revenue,
        Long totalServiceOrder,
        BigDecimal averageTicket,
        Long countPayment,
        Long distinctCustomers,
        List<FinancialDashboardGroupByDateDTO> revenueGroupedByDate,
        Map<PaymentMethod, BigDecimal>  paymentByMethod,
        Map<ServiceOrderStatus, Long>  serviceOrderByStatus,
        List<BestSellingServiceDTO> bestSellingService

) {
}
