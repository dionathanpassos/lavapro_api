package com.dionathan.lavapro.report.dto;

import com.dionathan.lavapro.dashboard.dto.BestSellingServiceDTO;
import com.dionathan.lavapro.dashboard.dto.FinancialDashboardGroupByDateDTO;
import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record FinancialReportDTO(
        BigDecimal revenue,
        Long countPayment,
        BigDecimal averageTicket,
        Map<PaymentMethod, BigDecimal>  paymentByMethod,
        List<FinancialDashboardGroupByDateDTO> revenueGroupedByDate


) {
}
