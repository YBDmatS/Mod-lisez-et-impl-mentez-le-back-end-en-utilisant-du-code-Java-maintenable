package com.chatop.api.dto;

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
     */
    private String email;

    /**
     * User's password for login.
     */
    private String password;
}

