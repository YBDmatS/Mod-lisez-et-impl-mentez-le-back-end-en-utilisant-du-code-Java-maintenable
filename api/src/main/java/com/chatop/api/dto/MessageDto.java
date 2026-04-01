package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a message in the application.
 */
@Data
@AllArgsConstructor
public class MessageDto {
    
    /**
     * The ID of the user sending the message.
     * This field is required and must be a positive number.
     */
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    @JsonProperty("user_id")
    long userId;

    /**
     * The ID of the rental associated with the message.
     * This field is required and must be a positive number.
     */
    @NotNull(message = "Rental ID is required")
    @Positive(message = "Rental ID must be a positive number")
    @JsonProperty("rental_id")
    long rentalId;

    /**
     * The content of the message.
     * This field is required and cannot be blank.
     */
    @NotBlank(message = "Message is required")
    String message;
}
