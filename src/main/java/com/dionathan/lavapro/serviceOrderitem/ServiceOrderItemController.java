package com.dionathan.lavapro.serviceOrderitem;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemRequestDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemResponseDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceOrderItemController {

    private final ServiceOrderItemService serviceOrderItemService;

    @PostMapping("/api/service-orders/{serviceOrderId}/items")
    public ResponseEntity<ServiceOrderItemResponseDTO> create(@PathVariable Long serviceOrderId, @RequestBody @Valid ServiceOrderItemRequestDTO requestDTO) {
        ServiceOrderItemResponseDTO created = serviceOrderItemService.create(serviceOrderId, requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/api/service-orders/{serviceOrderId}/items")
    public ResponseEntity<List<ServiceOrderItemResponseDTO>> findAll(@PathVariable Long serviceOrderId) {
        List<ServiceOrderItemResponseDTO> items = serviceOrderItemService.findAll(serviceOrderId);

        return ResponseEntity.ok(items);
    }

    @PatchMapping("/api/service-order-items/{itemId}")
    public ResponseEntity<ServiceOrderItemResponseDTO> update(@PathVariable Long itemId, @Valid @RequestBody ServiceOrderItemUpdateRequestDTO requestDTO) {
        ServiceOrderItemResponseDTO updated = serviceOrderItemService.update(itemId, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/api/service-order-items/{itemId}/delete")
    public ResponseEntity<ServiceOrderItemResponseDTO> delete(@PathVariable Long itemId) {
        serviceOrderItemService.delete(itemId);

        return ResponseEntity.noContent().build();
    }
}


