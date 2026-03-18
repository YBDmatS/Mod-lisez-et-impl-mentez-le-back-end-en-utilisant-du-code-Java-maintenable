package com.chatop.api.dto;

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
public class UserRegisterRequestDto {

    /**
     * User's full name for registration.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * User's email address for registration.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * User's password for registration.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
    private String password;
}
