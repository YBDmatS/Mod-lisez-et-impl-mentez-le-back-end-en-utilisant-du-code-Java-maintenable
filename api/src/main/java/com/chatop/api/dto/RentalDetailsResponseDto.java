package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RentalDetailsResponseDto {

    /**
     * Unique identifier for the rental property.
     */
    long id;

    /**
     * Name of the rental property.
     */
    private String name;

    /**
     * Surface area of the rental property in square meters.
     */
    private BigDecimal surface;

    /**
     * Price per night for renting the property.
     */
    private BigDecimal price;

    /**
     * Picture url of the rental property.
     */
    @JsonProperty("picture")
    private String pictureUrl;

    /**
     * Description of the rental property.
     */
    private String description;

    /**
     * Unique identifier of the owner of the rental property.
     */
    @JsonProperty("owner_id")
    private long ownerId;
    /**
     * Timestamp of when the rental property was created, formatted as "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the rental property was last updated, formatted as "yyyy/MM/dd".
     */
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;
}
