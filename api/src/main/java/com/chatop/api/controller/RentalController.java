package com.chatop.api.controller;

import com.chatop.api.dto.*;
import com.chatop.api.exception.ApiErrorResponse;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling rental-related endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Rental listing management endpoints")
public class RentalController {

    private final RentalService rentalService;

    /**
     * Endpoint to create a new rental based on the provided rental request details.
     *
     * @param request The rental request containing necessary information for creating a new rental.
     * @return HTTP 200 OK with a standard response containing the ID of the created rental if successful.
     */
    @PostMapping("/rentals")
    @Operation(summary = "Create a new rental", description = "Creates a new rental listing owned by the authenticated user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = RentalCreateRequestDto.class)))
    @ApiResponse(responseCode = "200", description = "Rental created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid rental data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "413", description = "Picture file size exceeds the maximum allowed limit",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "415", description = "Unsupported media type — request must be multipart/form-data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<StandardResponseDto> createRental(@Valid @ModelAttribute RentalCreateRequestDto request, @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        StandardResponseDto response = rentalService.createRental(request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve a list of rentals.
     *
     * @return HTTP 200 OK with a response containing the list of rentals.
     */
    @GetMapping("/rentals")
    @Operation(summary = "Get all rentals", description = "Returns the list of all available rental listings.")
    @ApiResponse(responseCode = "200", description = "Rentals retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RentalsResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<RentalsResponseDto> getRentals() {
        RentalsResponseDto response = rentalService.getRentals();
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve the details of a specific rental by its ID.
     *
     * @param rentalId The ID of the rental to retrieve details for.
     * @return HTTP 200 OK with a response containing the details of the specified rental if found.
     * @throws ResourceNotFoundException when no rental with the provided ID is found in the database (HTTP 404)
     */
    @GetMapping("/rentals/{rentalId}")
    @Operation(summary = "Get rental details", description = "Returns the full details of a specific rental identified by its ID.")
    @ApiResponse(responseCode = "200", description = "Rental details retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RentalDetailsResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid rentalId format (must be a numeric value)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Rental not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<RentalDetailsResponseDto> getRentalDetails(
            @Parameter(description = "ID of the rental to retrieve", example = "1", required = true)
            @PathVariable Long rentalId) {
        RentalDetailsResponseDto response = rentalService.getRentalDetails(rentalId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update an existing rental with the provided rental request details.
     *
     * @param request The rental request containing necessary information for updating the rental.
     * @param jwt     The JWT token containing the user's authentication information, automatically injected by Spring Security.
     * @return HTTP 200 OK with a standard response containing the ID of the updated rental if successful.
     */
    @PutMapping("/rentals/{rentalId}")
    @Operation(summary = "Update a rental", description = "Updates the details of an existing rental. Only the owner can perform this action.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = RentalUpdateRequestDto.class)))
    @ApiResponse(responseCode = "200", description = "Rental updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid rentalId format or invalid rental data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Rental not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<StandardResponseDto> updateRental(
            @Parameter(description = "ID of the rental to update", example = "1", required = true)
            @PathVariable Long rentalId,
            @Valid @ModelAttribute RentalUpdateRequestDto request,
            @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        StandardResponseDto response = rentalService.updateRental(rentalId, request, userId);
        return ResponseEntity.ok(response);
    }
}
