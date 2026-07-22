package com.dionathan.lavapro.dashboard;

import com.dionathan.lavapro.dashboard.dto.DateRangeDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Component
public class DateManager {

    public DateRangeDTO getCustomDayRange(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);

        return new DateRangeDTO(startDate, endDate);
    }

    public DateRangeDTO getCurrentMonthRange() {
        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endDate = today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        return new DateRangeDTO(startDate, endDate);
    }
}
