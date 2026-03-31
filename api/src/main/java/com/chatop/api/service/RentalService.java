package com.chatop.api.service;

import com.chatop.api.dto.RentalDto;
import com.chatop.api.dto.RentalRequestDto;
import com.chatop.api.dto.RentalsResponseDto;
import com.chatop.api.dto.StandardResponseDto;
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
    public StandardResponseDto createRental(@Valid RentalRequestDto request, long userId) {

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

    public RentalsResponseDto getRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        RentalsResponseDto response = new RentalsResponseDto();
        response.setRentals(new ArrayList<>());

        for (Rental rental : rentals) {
            response.getRentals().add(modelMapper.map(rental, RentalDto.class));
        }
        return response;
    }
}
