package com.chatop.api.exception;

import java.time.LocalDateTime;

/**
 * Standard API error response payload.
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
