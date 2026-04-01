package com.chatop.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for rentals response, containing a list of rental properties.
 */
@Data
@NoArgsConstructor
public class RentalsResponseDto {

    /**
     * List of rental properties to be included in the response.
     */
    private List<RentalDetailsResponseDto> rentals;
}
