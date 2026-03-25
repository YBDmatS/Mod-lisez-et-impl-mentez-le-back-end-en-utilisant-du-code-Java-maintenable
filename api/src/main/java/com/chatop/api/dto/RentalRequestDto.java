package com.chatop.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for rental creation request.
 */
@Data
@AllArgsConstructor
public class RentalRequestDto {

    /**
     * Name of the rental property.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Surface area of the rental property in square meters.
     * This field is required and must be a positive number.
     */
    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be a positive number")
    private BigDecimal surface;

    /**
     * Price per night for renting the property.
     * This field is required and must be a positive number.
     */
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    /**
     * URL of the picture representing the rental property.
     * This field is required and must not be blank.
     */
//    @JsonProperty("picture")
//    private String pictureUrl;

    /**
     * Description of the rental property.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Description is required")
    private String description;
}
