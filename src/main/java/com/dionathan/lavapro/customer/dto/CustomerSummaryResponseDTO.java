package com.dionathan.lavapro.customer.dto;

import java.time.LocalDateTime;

public record CustomerSummaryResponseDTO(
        Long id,
        String name,
        String phone

) {
}
