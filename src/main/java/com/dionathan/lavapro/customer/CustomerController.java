package com.dionathan.lavapro.customer;

import com.dionathan.lavapro.customer.dto.CustomerDetailsResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerRequestDTO;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerUpdateRequestDTO;
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
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@RequestBody @Valid CustomerRequestDTO requestDTO) {
        CustomerResponseDTO created = customerService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsResponseDTO> findById(@PathVariable Long id) {

        CustomerDetailsResponseDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CustomerUpdateRequestDTO requestDTO) {
        CustomerResponseDTO updated = customerService.update(id, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> findAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String search
    ) {
        Page<CustomerResponseDTO> customers = customerService.findAll(name, phone, plate, search, pageable);

        return ResponseEntity.ok(customers);
    }


}
