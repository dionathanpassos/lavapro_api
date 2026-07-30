package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.cashFlow.dto.CashFlowResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cash-flows")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    @GetMapping
    public ResponseEntity<Page<CashFlowResponseDTO>> findAll(
            @RequestParam(required = false) CashFlowCategory category,
            @RequestParam(required = false) CashFlowType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        Page<CashFlowResponseDTO> cashFlows = cashFlowService.findAll(category, type, startDate, endDate, pageable );

        return ResponseEntity.ok(cashFlows);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashFlowResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cashFlowService.findById(id));
    }
}
