package com.chatop.api.config;

import com.chatop.api.config.properties.PictureStorageProperties;
import com.chatop.api.dto.RentalCreateRequestDto;
import com.chatop.api.dto.RentalDetailsResponseDto;
import com.chatop.api.dto.RentalUpdateRequestDto;
import com.chatop.api.model.Rental;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper bean. This class defines how ModelMapper should map between Entity and DTO.
 */
@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    public final PictureStorageProperties pictureProperties;

    /**
     * Configures and returns a ModelMapper bean with custom mappings for User and Rental entities to their respective DTOs.
     *
     * @return a configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<RentalCreateRequestDto, Rental> rentalRequesTypeMap =
                modelMapper.createTypeMap(RentalCreateRequestDto.class, Rental.class);
        rentalRequesTypeMap.addMappings(mapper -> {
            mapper.map(RentalCreateRequestDto::getSurface, Rental::setSurfaceArea);
            mapper.map(RentalCreateRequestDto::getPrice, Rental::setPricePerNight);
            mapper.skip(Rental::setId);
            mapper.skip(Rental::setOwner);
            mapper.skip(Rental::setPictureUrl);
            mapper.skip(Rental::setCreatedAt);
            mapper.skip(Rental::setUpdatedAt);
        });

        TypeMap<RentalUpdateRequestDto, Rental> rentalUpdateTypeMap =
                modelMapper.createTypeMap(RentalUpdateRequestDto.class, Rental.class);
        rentalUpdateTypeMap.addMappings(mapper -> {
            mapper.map(RentalUpdateRequestDto::getSurface, Rental::setSurfaceArea);
            mapper.map(RentalUpdateRequestDto::getPrice, Rental::setPricePerNight);
            mapper.skip(Rental::setId);
            mapper.skip(Rental::setOwner);
            mapper.skip(Rental::setPictureUrl);
            mapper.skip(Rental::setCreatedAt);
            mapper.skip(Rental::setUpdatedAt);
        });

        TypeMap<Rental, RentalDetailsResponseDto> rentalResponseTypeMap =
                modelMapper.createTypeMap(Rental.class, RentalDetailsResponseDto.class);
        rentalResponseTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getOwner().getId(), RentalDetailsResponseDto::setOwnerId);
            mapper
                    .using(ctx -> buildPictureUrl((String) ctx.getSource()))
                    .map(Rental::getPictureUrl, RentalDetailsResponseDto::setPictureUrl);
            mapper.map(Rental::getSurfaceArea, RentalDetailsResponseDto::setSurface);
            mapper.map(Rental::getPricePerNight, RentalDetailsResponseDto::setPrice);
        });

        return modelMapper;
    }

    /**
     * Builds the full picture URL by combining the base URL from properties with the provided picture URL.
     *
     * @param pictureUrl the relative picture URL to be combined with the base URL
     * @return the full picture URL or null if the input pictureUrl is null or blank
     */
    private String buildPictureUrl(String pictureUrl) {
        if (pictureUrl == null || pictureUrl.isBlank()) {
            return null;
        }
        String baseUrl = pictureProperties.getBaseUrl();
        if (baseUrl.endsWith("/") && pictureUrl.startsWith("/")) {
            return baseUrl + pictureUrl.substring(1);
        }
        if (!baseUrl.endsWith("/") && !pictureUrl.startsWith("/")) {
            return baseUrl + "/" + pictureUrl;
        }
        return baseUrl + pictureUrl;
    }
}
