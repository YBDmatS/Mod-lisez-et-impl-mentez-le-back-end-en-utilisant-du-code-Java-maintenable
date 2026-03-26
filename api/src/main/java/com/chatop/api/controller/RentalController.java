package com.chatop.api.controller;

import com.chatop.api.dto.RentalRequestDto;
import com.chatop.api.dto.StandardResponseDto;
import com.chatop.api.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling rental-related endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
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
        log.debug("Received request to create a new rental from user {}", userId);

        StandardResponseDto response = rentalService.createRental(request, userId);
        log.debug("Successfully created a new rental with id: {}", response.getMessage());
        return ResponseEntity.ok(response);
    }
}
