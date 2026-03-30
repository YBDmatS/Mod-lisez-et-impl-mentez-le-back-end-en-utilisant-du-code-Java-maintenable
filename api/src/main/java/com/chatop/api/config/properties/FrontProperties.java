package com.chatop.api.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for front-end settings, such as the domain of the front-end application.
 */
@ConfigurationProperties(prefix = "front")
@Validated
@Getter
@Setter
public class FrontProperties {

    @NotBlank
    private String mainDomain;
}
