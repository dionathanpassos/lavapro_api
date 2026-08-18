package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.payment.dto.PaymentResponseDTO;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderDetailsResponseDTO(
        Long id,
        BigDecimal totalAmount,
        String observations,
        ServiceOrderStatus status,
        VehicleSummaryResponseDTO vehicle,
        List<ServiceOrderItemResponseDTO> items,
        List<PaymentResponseDTO> payments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
