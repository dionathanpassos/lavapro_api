package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogRequestDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogUpdateRequestDTO;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-catalogs")
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    @PostMapping
    public ResponseEntity<ServiceCatalogResponseDTO> create(@RequestBody @Valid ServiceCatalogRequestDTO requestDTO) {
        ServiceCatalogResponseDTO created = serviceCatalogService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceCatalogResponseDTO> update(@PathVariable Long id, @RequestBody @Valid ServiceCatalogUpdateRequestDTO requestDTO) {
        ServiceCatalogResponseDTO updated = serviceCatalogService.update(id, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCatalogResponseDTO> findById(@PathVariable Long id) {
        ServiceCatalogResponseDTO serviceCatalog = serviceCatalogService.findById(id);

        return ResponseEntity.ok(serviceCatalog);
    }

    @GetMapping
    public ResponseEntity<Page<ServiceCatalogResponseDTO>> findAll(Pageable pageable) {
        Page<ServiceCatalogResponseDTO> serviceCatalogs = serviceCatalogService.findAll(pageable);

        return ResponseEntity.ok(serviceCatalogs);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        serviceCatalogService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        serviceCatalogService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
