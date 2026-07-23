package com.trip10.Trip10.exception;

import tools.jackson.databind.ObjectMapper;
import com.trip10.Trip10.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles requests that reach a protected endpoint with no token or an invalid
 * one. Spring Security intercepts these before the DispatcherServlet, so
 * GlobalExceptionHandler never sees them — this is what keeps the response
 * body consistent (same ApiResponse JSON shape) for that case too.
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponse.unauthorized("Authentication required"))
        );
    }
}
