package com.chatop.api.security;

import com.chatop.api.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * CustomAuthenticationEntryPoint is a Spring Security component that handles authentication exceptions.
 * When a user tries to access a protected resource without being authenticated,
 * this handler will return a JSON response with an appropriate error message and HTTP status code 401 Unauthorized.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Handles AuthenticationException by returning a JSON response with error details and HTTP status 401 Unauthorized.
     *
     * @param request       the HttpServletRequest that resulted in the AuthenticationException
     * @param response      the HttpServletResponse to which the error response will be written
     * @param authException the AuthenticationException that was thrown
     * @throws IOException if an input or output exception occurs while writing the response
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Authentication is required to access this resource.",
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}