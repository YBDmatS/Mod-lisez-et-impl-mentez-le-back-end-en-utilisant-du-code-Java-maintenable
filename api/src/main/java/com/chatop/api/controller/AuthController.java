package com.chatop.api.controller;

import com.chatop.api.dto.UserJwtResponseDto;
import com.chatop.api.dto.UserLoginRequestDto;
import com.chatop.api.dto.UserMeResponseDto;
import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request The user registration request containing necessary information for creating a new user account.
     * @return HTTP 200 OK if registration is successful
     * @throws UserAlreadyExistsException when user registration fails due to an existing user (HTTP 409)
     */
    @PostMapping("/register")
    public ResponseEntity<UserJwtResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto request) {

        log.debug("Received request to register user for email: {}", request.getEmail());

        UserJwtResponseDto response = userService.register(request);
        log.debug("Successfully register user for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user with the provided login credentials and returns a JWT token if successful.
     *
     * @param request The user login request containing the email and password for authentication.
     * @return HTTP 200 OK with a JWT token if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<UserJwtResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {

        log.debug("Received request to login user for email: {}", request.getEmail());

        UserJwtResponseDto response = userService.login(request);
        log.debug("Successfully login user for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the current authenticated user's information based on the provided JWT token.
     *
     * @param jwt The JWT token containing the user's authentication information, automatically injected by Spring Security.
     * @return HTTP 200 OK with the current user's information if the JWT token is valid and the user is authenticated
     */
    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        log.debug("Received request to get current user for jwt: {}", userId);

        UserMeResponseDto response = userService.getCurrentUser(userId);
        log.debug("Successfully retrieved current user for userId: {}", response.getId());
        return ResponseEntity.ok(response);
    }
}
