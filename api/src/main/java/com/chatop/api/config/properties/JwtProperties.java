package com.chatop.api.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for JWT (JSON Web Token) settings.
 */
@ConfigurationProperties(prefix = "security.jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

    /**
     * Secret key used for signing and verifying JWT tokens.
     * This field is required and must not be blank.
     */
    @NotBlank
    private String secret;
}
