package com.chatop.api.service;

import com.chatop.api.dto.RentalRequestDto;
import com.chatop.api.dto.StandardResponseDto;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
import com.chatop.api.repository.RentalRepository;
import com.chatop.api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    /**
     * Creates a new rental based on the provided rental request details.
     *
     * @param request The rental request containing necessary information for creating a new rental.
     * @return A standard response with successfully message.
     */
    public StandardResponseDto createRental(@Valid RentalRequestDto request, long userId) {
        log.debug("Creating rental");

        Rental rental = modelMapper.map(request, Rental.class);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to create rental: owner not found with id: {}", userId);
                    return new IllegalArgumentException("Owner not found");
                });

        rental.setOwner(owner);

        rental.setPictureUrl("test.com/picture.jpg");
        
        rentalRepository.save(rental);
        log.debug("Rental created successfully with id: {}", rental.getId());

        return new StandardResponseDto("Rental created !");
    }
}
