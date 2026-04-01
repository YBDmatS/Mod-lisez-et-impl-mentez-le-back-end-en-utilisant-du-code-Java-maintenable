package com.chatop.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for standard responses.
 * Contains a message to be returned in API responses.
 */
@Data
@AllArgsConstructor
@Schema(name = "SuccessResponse", description = "Generic success response containing a confirmation message")
public class StandardResponseDto {

    /**
     * Message to be included in the API response.
     */
    @Schema(description = "Confirmation message", example = "Rental created !")
    private String message;
}
