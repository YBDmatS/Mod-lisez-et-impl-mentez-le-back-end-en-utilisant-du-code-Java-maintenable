package com.chatop.api.service;

import com.chatop.api.dto.*;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
import com.chatop.api.repository.RentalRepository;
import com.chatop.api.repository.UserRepository;
import com.chatop.api.service.storage.PictureStorageService;
import com.chatop.api.service.storage.StoredPicture;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for handling rental-related business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;
    private final PictureStorageService pictureStorageService;

    /**
     * Creates a new rental based on the provided rental request details.
     *
     * @param request The rental request containing necessary information for creating a new rental.
     * @return A standard response with successfully message.
     */
    public StandardResponseDto createRental(@Valid RentalCreateRequestDto request, long userId) {

        Rental rental = modelMapper.map(request, Rental.class);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Failed to create rental: owner not found with id: {}", userId);
                    return new IllegalArgumentException("Owner not found");
                });
        rental.setOwner(owner);

        StoredPicture picture = pictureStorageService.storePicture(request.getPicture());
        rental.setPictureUrl(picture.relativeUrl());

        try {
            rentalRepository.save(rental);
        } catch (Exception e) {
            try {
                pictureStorageService.deleteByStoragePath(picture);
            } catch (Exception cleanupException) {
                log.error("Failed to delete stored picture during rental rollback: {}", picture.storagePath(), cleanupException);
            }

            log.error("Failed to create rental: error saving rental to database", e);
            throw e;
        }
        return new StandardResponseDto("Rental created !");
    }

    /**
     * Retrieves a list of rentals. This method is accessible to all users, including unauthenticated ones.
     *
     * @return A response containing the list of rentals.
     */
    public RentalsResponseDto getRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        RentalsResponseDto response = new RentalsResponseDto();
        response.setRentals(new ArrayList<>());

        for (Rental rental : rentals) {
            response.getRentals().add(modelMapper.map(rental, RentalDetailsResponseDto.class));
        }
        return response;
    }

    /**
     * Retrieves the details of a specific rental by its ID.
     *
     * @param rentalId The ID of the rental to retrieve details for.
     * @return A response containing the details of the specified rental.
     */
    public RentalDetailsResponseDto getRentalDetails(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .map(rental -> modelMapper.map(rental, RentalDetailsResponseDto.class))
                .orElseThrow(() -> {
                    log.error("Failed to get rental details: rental not found with id: {}", rentalId);
                    return new IllegalArgumentException("Rental not found");
                });
    }

    /**
     * Updates an existing rental with the provided rental request details.
     * Only the owner of the rental is allowed to perform this operation.
     *
     * @param rentalId The ID of the rental to update.
     * @param request  The rental request containing necessary information for updating the rental.
     * @param userId   The ID of the user attempting to update the rental, used for authorization checks.
     * @return A standard response with successfully message if the update is successful.
     */
    public StandardResponseDto updateRental(Long rentalId, @Valid RentalUpdateRequestDto request, long userId) {

        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> {
            log.error("Failed to get rental details to update: rental not found with id: {}", rentalId);
            return new IllegalArgumentException("Rental not found");
        });

        if (rental.getOwner() == null || rental.getOwner().getId() != userId) {
            log.error("Failed to update rental: user {} is not the owner of rental {}", userId, rentalId);
            throw new IllegalArgumentException("You are not allowed to update this rental");
        }

        modelMapper.map(request, rental);
        rentalRepository.save(rental);
        return new StandardResponseDto("Rental updated !");
    }
}
