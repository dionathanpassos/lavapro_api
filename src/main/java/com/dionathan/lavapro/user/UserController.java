package com.dionathan.lavapro.user;

import com.dionathan.lavapro.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO requestDTO) {
        UserResponseDTO created = userService.create(requestDTO);

        return ResponseEntity.ok(created);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserResponseDTO> users = userService.findAll(name, active, pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/indicators")
    public ResponseEntity<UserIndicatorsDTO> getIndicators (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ) {
        UserIndicatorsDTO indicators = userService.getIndicators(name, active);

        return ResponseEntity.ok(indicators);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequestDTO requestDTO) {
        UserResponseDTO updated = userService.update(id, requestDTO);

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> deactivate(@PathVariable Long id) {
        UserResponseDTO user = userService.deactivate(id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> activate(@PathVariable Long id) {
        UserResponseDTO user = userService.activate(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> profile() {
        UserResponseDTO profile = userService.profile();

        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody @Valid UserUpdateProfileRequestDTO requestDTO) {
        UserResponseDTO updated = userService.updateProfile(requestDTO);

        return ResponseEntity.ok(updated);
    }

}
