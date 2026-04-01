package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user JWT response.
 * Contains the authentication token for the logged-in user.
 */
@Data
@AllArgsConstructor
@Schema(name = "AuthToken", description = "JWT token returned after successful authentication or registration")
public class UserJwtResponseDto {

    @Schema(description = "JWT Bearer token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}
