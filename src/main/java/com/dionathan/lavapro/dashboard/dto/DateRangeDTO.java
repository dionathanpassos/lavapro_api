package com.dionathan.lavapro.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DateRangeDTO(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
