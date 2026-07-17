package com.dionathan.lavapro.payment;

import com.dionathan.lavapro.payment.dto.PaymentResponseDTO;
import com.dionathan.lavapro.payment.dto.PaymentRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO created = paymentService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id{")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{paymentId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long paymentId){
        paymentService.cancel(paymentId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> payments = paymentService.findAll();

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable Long paymentId) {
        PaymentResponseDTO payment = paymentService.findById(paymentId);

        return ResponseEntity.ok(payment);
    }

}
