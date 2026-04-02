package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for rental property response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RentalDetails", description = "Rental property details")
public class RentalDetailsResponseDto {

    /**
     * Unique identifier for the rental property.
     */
    @Schema(description = "Unique rental identifier", example = "1")
    long id;

    /**
     * Name of the rental property.
     */
    @Schema(description = "Name of the rental property", example = "Cozy mountain cabin")
    private String name;

    /**
     * Surface area of the rental property in square meters.
     */
    @Schema(description = "Surface area in square meters", example = "45.5")
    private BigDecimal surface;

    /**
     * Price per night for renting the property.
     */
    @Schema(description = "Price per night in euros", example = "120.00")
    private BigDecimal price;

    /**
     * Picture url of the rental property.
     */
    @Schema(description = "Public URL of the rental picture", example = "http://localhost:3001/pictures/abc123.jpg")
    @JsonProperty("picture")
    private String pictureUrl;

    /**
     * Description of the rental property.
     */
    @Schema(description = "Description of the rental property", example = "A beautiful cabin in the mountains with a stunning view.")
    private String description;

    /**
     * Unique identifier of the owner of the rental property.
     */
    @Schema(description = "ID of the rental owner", example = "2")
    @JsonProperty("owner_id")
    private long ownerId;
    /**
     * Timestamp of when the rental property was created, formatted as "yyyy/MM/dd".
     */
    @Schema(description = "Creation date (yyyy/MM/dd)", example = "2024/01/15")
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the rental property was last updated, formatted as "yyyy/MM/dd".
     */
    @Schema(description = "Last update date (yyyy/MM/dd)", example = "2024/06/20")
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;
}
