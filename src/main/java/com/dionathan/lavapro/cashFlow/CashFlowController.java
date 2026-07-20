package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.cashFlow.dto.CashFlowResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cash-flows")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    @GetMapping
    public ResponseEntity<List<CashFlowResponseDTO>> findAll() {
        List<CashFlowResponseDTO> cashFlows = cashFlowService.findAll();

        return ResponseEntity.ok(cashFlows);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashFlowResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cashFlowService.findById(id));
    }
}
