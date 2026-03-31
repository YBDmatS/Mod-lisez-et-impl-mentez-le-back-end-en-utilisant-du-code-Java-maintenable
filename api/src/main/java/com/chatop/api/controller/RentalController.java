package com.chatop.api.controller;

import com.chatop.api.dto.*;
import com.chatop.api.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class RentalController {

    private final RentalService rentalService;

    /**
     * Endpoint to create a new rental based on the provided rental request details.
     *
     * @param request The rental request containing necessary information for creating a new rental.
     * @return HTTP 200 OK with a standard response containing the ID of the created rental if successful.
     */
    @PostMapping("/rentals")
    public ResponseEntity<StandardResponseDto> createRental(@Valid @ModelAttribute RentalRequestDto request, @AuthenticationPrincipal Jwt jwt) {
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
    public ResponseEntity<RentalsResponseDto> getRentals() {
        RentalsResponseDto response = rentalService.getRentals();
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve the details of a specific rental by its ID.
     *
     * @param rentalId The ID of the rental to retrieve details for.
     * @return HTTP 200 OK with a response containing the details of the specified rental if found.
     */
    @GetMapping("/rentals/{rentalId}")
    public ResponseEntity<RentalDto> getRentalDetails(@PathVariable Long rentalId) {
        RentalDto response = rentalService.getRentalDetails(rentalId);
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
    public ResponseEntity<StandardResponseDto> updateRental(
            @PathVariable Long rentalId,
            @Valid @ModelAttribute RentalUpdateDto request,
            @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        StandardResponseDto response = rentalService.updateRental(rentalId, request, userId);
        return ResponseEntity.ok(response);
    }
}
