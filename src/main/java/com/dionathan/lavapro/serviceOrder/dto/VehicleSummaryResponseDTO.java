package com.dionathan.lavapro.serviceOrder.dto;

import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;

import java.time.LocalDateTime;

public record VehicleSummaryResponseDTO(
        Long id,
        String plate,
        String model,
        String brand,
        String color,
        int year,
        CustomerSummaryResponseDTO customer

) {
}
