package com.dionathan.lavapro.dashboard;

import com.dionathan.lavapro.dashboard.dto.DashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        DashboardResponseDTO summary = dashboardService.getDashboard();

        return ResponseEntity.ok(summary);
    }
}
