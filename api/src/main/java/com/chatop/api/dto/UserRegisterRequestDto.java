package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user registration request.
 * Contains user details for registration and a method to convert to User entity.
 */
@Data
@AllArgsConstructor
@Schema(name = "RegisterRequest", description = "Request payload for registering a new user account")
public class UserRegisterRequestDto {

    /**
     * User's full name for registration.
     * This field is required and must not be blank.
     */
    @Schema(description = "User's full name", example = "John Doe")
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * User's email address for registration.
     * This field is required and must not be blank.
     */
    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * User's password for registration.
     * This field is required and must not be blank.
     */
    @Schema(description = "Password — min 8 characters, must include at least one uppercase letter, one lowercase letter, one digit and one special character", example = "P@ssw0rd!")
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
    private String password;
}
