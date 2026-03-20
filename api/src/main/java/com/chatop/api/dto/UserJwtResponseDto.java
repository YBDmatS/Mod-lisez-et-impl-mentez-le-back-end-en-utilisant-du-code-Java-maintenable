package com.chatop.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user JWT response.
 * Contains the authentication token for the logged-in user.
 */
@Data
@AllArgsConstructor
public class UserJwtResponseDto {

    /**
     * JWT token for the authenticated user.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "JWT token is required")
    private String jwt;
}
