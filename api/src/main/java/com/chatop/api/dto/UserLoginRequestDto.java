package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user login request.
 * Contains email and password for authentication.
 */
@Data
@AllArgsConstructor
@Schema(name = "LoginRequest", description = "Request payload for authenticating a user")
public class UserLoginRequestDto {

    /**
     * User's email address for login.
     * This field is required and must not be blank.
     */
    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * User's password for login.
     * This field is required and must not be blank.
     */
    @Schema(description = "User's password", example = "P@ssw0rd!")
    @NotBlank(message = "Password is required")
    private String password;
}

