package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "UpdateRentalRequest", description = "Request payload for updating an existing rental listing")
public class RentalUpdateRequestDto {

    /**
     * Name of the rental property.
     * This field is required and must not be blank.
     */
    @Schema(description = "Name of the rental property", example = "Cozy mountain cabin")
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Surface area of the rental property in square meters.
     * This field is required and must be a positive number.
     */
    @Schema(description = "Surface area in square meters", example = "45.5")
    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be a positive number")
    private BigDecimal surface;

    /**
     * Price per night for renting the property.
     * This field is required and must be a positive number.
     */
    @Schema(description = "Price per night in euros", example = "120.00")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    /**
     * Description of the rental property.
     * This field is required and must not be blank.
     */
    @Schema(description = "Description of the rental property", example = "A beautiful cabin in the mountains with a stunning view.")
    @NotBlank(message = "Description is required")
    private String description;
}
