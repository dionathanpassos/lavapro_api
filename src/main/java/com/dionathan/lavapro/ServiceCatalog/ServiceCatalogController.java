package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogIndicatorsDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogRequestDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<Page<ServiceCatalogResponseDTO>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ServiceCatalogType type,
            @RequestParam(defaultValue = "true") Boolean active,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ServiceCatalogResponseDTO> serviceCatalogs = serviceCatalogService.findAll(search, type, active,pageable);

        return ResponseEntity.ok(serviceCatalogs);
    }

    @GetMapping("/indicators")
    public ResponseEntity<ServiceCatalogIndicatorsDTO> getIndicators() {
        ServiceCatalogIndicatorsDTO indicators = serviceCatalogService.getIndicators();

        return ResponseEntity.ok(indicators);
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
