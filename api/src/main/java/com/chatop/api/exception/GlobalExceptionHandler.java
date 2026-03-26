package com.chatop.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * It handles specific exceptions and returns appropriate HTTP responses with error details.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles UserAlreadyExistsException and returns a 409 Conflict response with error details.
     *
     * @param e       the exception to handle
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with error details and HTTP status 409 Conflict
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        String errorMessage = "A user with this email already exists";

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                errorMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request response with validation error details.
     *
     * @param e       the exception to handle
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with validation error details and HTTP status 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation error");

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                errorMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles authentication-related exceptions (BadCredentialsException and UsernameNotFoundException)
     * and returns a 401 Unauthorized response with error details.
     *
     * @param e       the exception to handle
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with error details and HTTP status 401 Unauthorized
     */
    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            Exception e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                "Invalid credentials",
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles MaxUploadSizeExceededException and returns a 413 Content Too Large response.
     *
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with error details and HTTP status 413
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceededException(HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(413);

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                "File size exceeds the maximum allowed limit",
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles HttpMediaTypeNotSupportedException and returns a 415 Unsupported Media Type response.
     *
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with error details and HTTP status 415
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                "Content type not supported. Expected: multipart/form-data",
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request response.
     *
     * @param e       the exception to handle
     * @param request the HTTP request that resulted in the exception
     * @return a ResponseEntity containing the ApiErrorResponse with error details and HTTP status 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                e.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }
}
