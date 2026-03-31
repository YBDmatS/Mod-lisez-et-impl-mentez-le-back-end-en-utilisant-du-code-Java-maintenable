package com.chatop.api.config;

import com.chatop.api.config.properties.PictureStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class for serving static resources, specifically for handling picture storage and access.
 */
@Configuration
@RequiredArgsConstructor
public class StaticResourceConfig implements WebMvcConfigurer {

    private final PictureStorageProperties pictureProperties;

    /**
     * Adds resource handlers to serve static resources from the specified upload directory.
     *
     * @param registry the ResourceHandlerRegistry to which resource handlers can be added
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(pictureProperties.getUploadDir()).toAbsolutePath().normalize();
        String resourceLocation = uploadPath.toUri().toString();

        registry.addResourceHandler(pictureProperties.getPublicPath())
                .addResourceLocations(resourceLocation);
    }
}
