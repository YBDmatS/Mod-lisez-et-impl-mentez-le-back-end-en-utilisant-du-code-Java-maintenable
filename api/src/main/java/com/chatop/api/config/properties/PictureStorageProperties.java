package com.chatop.api.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
     * Base URL for accessing stored pictures.
     * This field is required and must not be blank.
     */
    @NotBlank
    String baseUrl;
}
