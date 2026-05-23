package com.dionathan.lavapro.auth;

import com.dionathan.lavapro.auth.dto.AuthLoginRequestDTO;
import com.dionathan.lavapro.auth.dto.AuthResponseDTO;
import com.dionathan.lavapro.auth.dto.AuthSignUpRequestDTO;
import com.dionathan.lavapro.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid AuthSignUpRequestDTO requestDTO) {
        UserResponseDTO created = authService.register(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO requestDTO) {
        AuthResponseDTO token = authService.login(requestDTO);

        return ResponseEntity.ok(token);
    }

}
