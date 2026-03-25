package com.chatop.api.dto;

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
     */
    private String token;
}
