package com.chatop.api.controller;

import com.chatop.api.dto.UserDetailsResponseDto;
import com.chatop.api.dto.UserJwtResponseDto;
import com.chatop.api.dto.UserLoginRequestDto;
import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.ApiErrorResponse;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
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
    @SecurityRequirements
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token upon success.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserRegisterRequestDto.class)))
    @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserJwtResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid registration data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Email already in use",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<UserJwtResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto request) {
        UserJwtResponseDto response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user with the provided login credentials and returns a JWT token if successful.
     *
     * @param request The user login request containing the email and password for authentication.
     * @return HTTP 200 OK with a JWT token if authentication is successful
     */
    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Authenticate a user", description = "Validates user credentials and returns a JWT token upon success.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserLoginRequestDto.class)))
    @ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserJwtResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Missing or blank email / password",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<UserJwtResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {
        UserJwtResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the current authenticated user's information based on the provided JWT token.
     *
     * @param jwt The JWT token containing the user's authentication information, automatically injected by Spring Security.
     * @return HTTP 200 OK with the current user's information if the JWT token is valid and the user is authenticated
     * @throws ResourceNotFoundException when the authenticated user no longer exists in the database (HTTP 404)
     */
    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user", description = "Returns the profile of the currently authenticated user based on the provided JWT token.")
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDetailsResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Authenticated user no longer exists",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<UserDetailsResponseDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        UserDetailsResponseDto response = userService.getCurrentUser(userId);
        return ResponseEntity.ok(response);
    }
}
