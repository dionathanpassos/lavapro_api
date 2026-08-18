package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.vehicle.dto.VehicleDetailsResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleRequestDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.extensibility.VerbExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> create(@RequestBody @Valid VehicleRequestDTO requestDTO) {
        VehicleResponseDTO created = vehicleService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDetailsResponseDTO> findById(@PathVariable Long id) {
        VehicleDetailsResponseDTO vehicle = vehicleService.findById(id);

        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> update(@PathVariable Long id, @RequestBody @Valid VehicleUpdateRequestDTO requestDTO) {
        VehicleResponseDTO updated = vehicleService.update(id, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<VehicleDetailsResponseDTO>> findAll(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String search,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        Page<VehicleDetailsResponseDTO> vehicles = vehicleService.findAll(plate, customer, model, brand, search, pageable);

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/plate/{plate}")
    public ResponseEntity<VehicleResponseDTO> findByPlate(@PathVariable String plate) {
        VehicleResponseDTO vehicle = vehicleService.findByPlate(plate);

        return ResponseEntity.ok(vehicle);
    }


}
