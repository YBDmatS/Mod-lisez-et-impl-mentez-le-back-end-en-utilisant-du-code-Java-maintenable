package com.chatop.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user login request.
 * Contains email and password for authentication.
 */
@Data
@AllArgsConstructor
public class UserLoginRequestDto {

    /**
     * User's email address for login.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * User's password for login.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Password is required")
    private String password;
}

