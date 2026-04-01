package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for rentals response, containing a list of rental properties.
 */
@Data
@NoArgsConstructor
@Schema(name = "RentalList", description = "List of all available rental listings")
public class RentalsResponseDto {

    /**
     * List of rental properties to be included in the response.
     */
    @Schema(description = "Array of rental listings")
    private List<RentalDetailsResponseDto> rentals;
}
