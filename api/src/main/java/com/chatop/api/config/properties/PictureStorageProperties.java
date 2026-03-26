package com.chatop.api.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for picture storage settings.
 */
@ConfigurationProperties(prefix = "picture-storage")
@Validated
@Getter
@Setter
public class PictureStorageProperties {

    /**
     * Directory where pictures are stored.
     * This field is required and must not be blank.
     */
    @NotBlank
    String uploadDir;

    /**
     * Directory where pictures are publicly accessible.
     * This field is required and must not be blank.
     */
    @NotBlank
    String relativePublicUrl;

    /**
     * Maximum allowed file size for uploaded pictures (e.g., "5MB").
     * This field is required.
     */
    @NotNull
    DataSize maxFileSize;

    /**
     * Base URL for accessing stored pictures.
     * This field is required and must not be blank.
     */
    @NotBlank
    String baseUrl;
}
