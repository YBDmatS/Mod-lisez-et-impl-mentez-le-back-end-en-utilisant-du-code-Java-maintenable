package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user information response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserDetails", description = "User profile details")
public class UserDetailsResponseDto {

    /**
     * Unique identifier of the user.
     */
    @Schema(description = "Unique identifier of the user", example = "1")
    private long id;

    /**
     * User's full name.
     */
    @Schema(description = "User's full name", example = "John Doe")
    private String name;

    /**
     * User's email address.
     */
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    /**
     * Timestamp of when the user was created, formatted as "yyyy/MM/dd".
     */
    @Schema(description = "Account creation date (yyyy/MM/dd)", example = "2024/01/15")
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the user was last updated, formatted as "yyyy/MM/dd".
     */
    @Schema(description = "Last update date (yyyy/MM/dd)", example = "2024/06/20")
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;
}
