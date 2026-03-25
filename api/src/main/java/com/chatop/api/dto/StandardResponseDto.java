package com.chatop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for standard responses.
 * Contains a message to be returned in API responses.
 */
@Data
@AllArgsConstructor
public class StandardResponseDto {

    /**
     * Message to be included in the API response.
     */
    private String message;
}
