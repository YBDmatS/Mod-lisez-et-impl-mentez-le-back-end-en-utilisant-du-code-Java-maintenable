package com.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDetailsResponseDto {

    /**
     * Unique identifier of the user.
     */
    private long id;

    /**
     * User's full name.
     */
    private String name;

    /**
     * User's email address.
     */
    private String email;

    /**
     * Timestamp of when the user was created, formatted as "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the user was last updated, formatted as "yyyy/MM/dd".
     */
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;
}
