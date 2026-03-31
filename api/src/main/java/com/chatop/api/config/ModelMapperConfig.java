package com.chatop.api.config;

import com.chatop.api.config.properties.PictureStorageProperties;
import com.chatop.api.dto.RentalDto;
import com.chatop.api.dto.RentalRequestDto;
import com.chatop.api.dto.RentalUpdateDto;
import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
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

        TypeMap<User, UserRegisterRequestDto> userRegisterRequestTypeMap =
                modelMapper.createTypeMap(User.class, UserRegisterRequestDto.class);
        userRegisterRequestTypeMap.addMappings(mapper ->
                mapper.skip(UserRegisterRequestDto::setPassword));

        TypeMap<RentalRequestDto, Rental> rentalRequesTypeMap =
                modelMapper.createTypeMap(RentalRequestDto.class, Rental.class);
        rentalRequesTypeMap.addMappings(mapper -> {
            mapper.map(RentalRequestDto::getSurface, Rental::setSurfaceArea);
            mapper.map(RentalRequestDto::getPrice, Rental::setPricePerNight);
            mapper.skip(Rental::setId);
            mapper.skip(Rental::setOwner);
            mapper.skip(Rental::setPictureUrl);
            mapper.skip(Rental::setCreatedAt);
            mapper.skip(Rental::setUpdatedAt);
        });

        TypeMap<RentalUpdateDto, Rental> rentalUpdateTypeMap =
                modelMapper.createTypeMap(RentalUpdateDto.class, Rental.class);
        rentalUpdateTypeMap.addMappings(mapper -> {
            mapper.map(RentalUpdateDto::getSurface, Rental::setSurfaceArea);
            mapper.map(RentalUpdateDto::getPrice, Rental::setPricePerNight);
            mapper.skip(Rental::setId);
            mapper.skip(Rental::setOwner);
            mapper.skip(Rental::setPictureUrl);
            mapper.skip(Rental::setCreatedAt);
            mapper.skip(Rental::setUpdatedAt);
        });

        TypeMap<Rental, RentalDto> rentalResponseTypeMap =
                modelMapper.createTypeMap(Rental.class, RentalDto.class);
        rentalResponseTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getOwner().getId(), RentalDto::setOwnerId);
            mapper
                    .using(ctx -> buildPictureUrl((String) ctx.getSource()))
                    .map(Rental::getPictureUrl, RentalDto::setPictureUrl);
            mapper.map(Rental::getSurfaceArea, RentalDto::setSurface);
            mapper.map(Rental::getPricePerNight, RentalDto::setPrice);
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
