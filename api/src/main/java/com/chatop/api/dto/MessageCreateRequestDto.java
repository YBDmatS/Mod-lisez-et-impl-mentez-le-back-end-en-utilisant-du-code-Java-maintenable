package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SendMessageRequest", description = "Request payload for sending a message about a rental")
public class MessageCreateRequestDto {
    
    /**
     * The ID of the user sending the message.
     * This field is required and must be a positive number.
     */
    @Schema(description = "ID of the user sending the message", example = "1")
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    @JsonProperty("user_id")
    long userId;

    /**
     * The ID of the rental associated with the message.
     * This field is required and must be a positive number.
     */
    @Schema(description = "ID of the rental the message is about", example = "3")
    @NotNull(message = "Rental ID is required")
    @Positive(message = "Rental ID must be a positive number")
    @JsonProperty("rental_id")
    long rentalId;

    /**
     * The content of the message.
     * This field is required and cannot be blank.
     */
    @Schema(description = "Content of the message", example = "Hello, I'm interested in this rental!")
    @NotBlank(message = "Message is required")
    String message;
}
