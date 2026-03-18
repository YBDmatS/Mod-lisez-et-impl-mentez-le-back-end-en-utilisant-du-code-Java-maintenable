package com.chatop.api.config;

import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper bean. This class defines how ModelMapper should map between Entity and DTO.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates and configures a ModelMapper bean for mapping between User entity and UserRegisterRequestDto.
     *
     * @return ModelMapper bean with custom mappings defined for User to UserRegisterRequestDto.
     * The password field is skipped during mapping to ensure it is not exposed in the DTO.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<User, UserRegisterRequestDto> userRegisterRequestTypeMap = modelMapper.createTypeMap(User.class, UserRegisterRequestDto.class);
        userRegisterRequestTypeMap.addMappings(mapper -> mapper.skip(UserRegisterRequestDto::setPassword));

        return modelMapper;
    }
}
