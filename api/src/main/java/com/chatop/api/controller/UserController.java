package com.chatop.api.controller;

import com.chatop.api.dto.UserDetailsResponseDto;
import com.chatop.api.exception.ApiErrorResponse;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user-related endpoints, such as retrieving user details.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile management endpoints")
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to retrieve the details of a user by their ID.
     *
     * @param userId The ID of the user whose details are to be retrieved, extracted from the URL path variable.
     * @return HTTP 200 OK with a response containing the user's details if the user is found.
     * @throws ResourceNotFoundException when the user with the specified ID is not found in the database (HTTP 404)
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get user details", description = "Returns the profile details of a user identified by their ID.")
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDetailsResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid userId format (must be a numeric value)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(
            @Parameter(description = "ID of the user to retrieve", example = "1", required = true)
            @PathVariable("userId") Long userId) {
        UserDetailsResponseDto response = userService.getCurrentUser(userId);
        return ResponseEntity.ok(response);
    }
}
