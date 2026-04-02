package com.chatop.api.exception;

/**
 * Exception thrown when attempting to register a user that already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
