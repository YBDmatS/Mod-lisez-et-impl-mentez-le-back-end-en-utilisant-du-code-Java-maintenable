package com.chatop.api.service;

import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling user-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user based on the provided registration data transfer object.
     *
     * @param dto the user registration request data transfer object containing user details
     */
    public void register(UserRegisterRequestDto dto) {
        log.debug("Registering user");
        User user = modelMapper.map(dto, User.class);
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
    }
}
