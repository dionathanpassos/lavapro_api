package com.dionathan.lavapro.report.dto;

import com.dionathan.lavapro.dashboard.dto.BestSellingServiceDTO;
import com.dionathan.lavapro.report.projection.CustomerProjection;

import java.util.List;

public record OperationalReportDTO(
        ServiceOrderSummaryDTO serviceOrderSummary,
        ServiceOrderKpiDTO serviceOrderKpi,
        List<BestSellingServiceDTO> bestSellingServices
) {
}
