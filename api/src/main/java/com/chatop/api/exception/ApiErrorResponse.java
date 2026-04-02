package com.chatop.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Standard API error response payload.
 */
@Schema(name = "ErrorResponse", description = "Standard error response returned when a request fails")
public record ApiErrorResponse(
        @Schema(description = "Timestamp of the error", example = "2024-06-20T14:35:00")
        LocalDateTime timestamp,
        @Schema(description = "HTTP status code", example = "404")
        int status,
        @Schema(description = "HTTP status label", example = "NOT_FOUND")
        String error,
        @Schema(description = "Human-readable error message", example = "Rental not found")
        String message,
        @Schema(description = "Request path that caused the error", example = "/api/rentals/99")
        String path
) {
}
