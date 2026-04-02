package com.chatop.api.security;

import com.chatop.api.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * CustomAccessDeniedHandler is a Spring Security component that handles access denied exceptions.
 * When a user tries to access a resource they don't have permission for,
 * this handler will return a JSON response with an appropriate error message and HTTP status code 403 Forbidden.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * Handles AccessDeniedException by returning a JSON response with error details and HTTP status 403 Forbidden.
     *
     * @param request               the HttpServletRequest that resulted in the AccessDeniedException
     * @param response              the HttpServletResponse to which the error response will be written
     * @param accessDeniedException the AccessDeniedException that was thrown
     * @throws IOException if an input or output exception occurs while writing the response
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AccessDeniedException accessDeniedException
    ) throws IOException {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                "You are not allowed to access this resource.",
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}