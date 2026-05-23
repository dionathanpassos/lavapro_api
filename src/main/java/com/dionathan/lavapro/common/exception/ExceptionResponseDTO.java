package com.dionathan.lavapro.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ExceptionResponseDTO(
        int status,
        String error,
        String message,
        String path,
        String method,
        LocalDateTime timestamp,
        Map<String, String> fieldErrors
) {
}
