package com.dionathan.lavapro.common.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ExceptionResponseDTO error = new ExceptionResponseDTO(
                status.value(),
                "Unauthorized",
                "Token inválido ou expirado",
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now(),
                null
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("appication/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(error));

    }
}
