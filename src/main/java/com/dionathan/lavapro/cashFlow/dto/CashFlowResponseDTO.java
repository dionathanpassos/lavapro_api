package com.dionathan.lavapro.cashFlow.dto;

import com.dionathan.lavapro.cashFlow.CashFlowCategory;
import com.dionathan.lavapro.cashFlow.CashFlowType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashFlowResponseDTO(
        Long id,
        CashFlowType type,
        CashFlowCategory category,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long companyId,
        Long serviceOrderId,
        Long paymentId,
        String paymentMethod,
        String customerName,
        String vehiclePlate

) {
}
