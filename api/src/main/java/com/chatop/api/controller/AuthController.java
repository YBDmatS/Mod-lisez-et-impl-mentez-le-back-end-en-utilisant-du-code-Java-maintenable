package com.chatop.api.controller;

import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterRequestDto request) {

        log.debug("Received request to register user for email: {}", request.getEmail());

        userService.register(request);
        log.debug("Successfully register user for email: {}", request.getEmail());
        return ResponseEntity.ok().build();
    }
}
