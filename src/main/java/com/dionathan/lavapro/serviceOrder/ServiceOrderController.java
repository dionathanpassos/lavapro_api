package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderRequestDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<ServiceOrderResponseDTO> create(@RequestBody @Valid ServiceOrderRequestDTO requestDTO) {
        ServiceOrderResponseDTO created = serviceOrderService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> update(@PathVariable Long id, @RequestBody @Valid  ServiceOrderUpdateRequestDTO requestDTO) {
        ServiceOrderResponseDTO updated = serviceOrderService.update(id, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> findById(@PathVariable Long id) {
        ServiceOrderResponseDTO serviceOrder = serviceOrderService.findById(id);

        return ResponseEntity.ok(serviceOrder);
    }



    @GetMapping
    public ResponseEntity<Page<ServiceOrderResponseDTO>> findAdll(@RequestParam(required = false) ServiceOrderStatus status,Pageable pageable) {
        Page<ServiceOrderResponseDTO> serviceOrders = serviceOrderService.findAll(status, pageable);

        return ResponseEntity.ok(serviceOrders);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> start(@PathVariable Long id) {
        serviceOrderService.start(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finish(@PathVariable Long id) {
        serviceOrderService.finish(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<Void> deliver(@PathVariable Long id) {
        serviceOrderService.deliver(id);
        return ResponseEntity.noContent().build();
    }
}
