package com.chatop.api.service;

import com.chatop.api.dto.UserJwtResponseDto;
import com.chatop.api.dto.UserLoginRequestDto;
import com.chatop.api.dto.UserMeResponseDto;
import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user based on the provided registration data transfer object.
     *
     * @param dto the user registration request data transfer object containing user details
     * @throws UserAlreadyExistsException if a user with the same email already exists
     */
    public UserJwtResponseDto register(UserRegisterRequestDto dto) {
        log.debug("Registering user for email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("User registration failed: email {} is already used", dto.getEmail());
            throw new UserAlreadyExistsException("Email already used");
        }

        User user = modelMapper.map(dto, User.class);
        String encodedPassword = Objects.requireNonNull(
                passwordEncoder.encode(dto.getPassword()),
                "Encoded password must not be null"
        );
        user.setPasswordHash(encodedPassword);

        User savedUser = userRepository.save(user);

        Long userId = Objects.requireNonNull(
                savedUser.getId(),
                "Saved user id must not be null"
        );
        String jwt = jwtService.generateToken(userId);

        log.debug("User with email {} registered successfully", user.getEmail());
        return new UserJwtResponseDto(jwt);
    }

    /**
     * Authenticates a user based on the provided login data transfer object and returns a JWT token if successful.
     *
     * @param dto the user login request data transfer object containing email and password for authentication
     * @return a UserJwtResponseDto containing the generated JWT token if authentication is successful
     */
    public UserJwtResponseDto login(UserLoginRequestDto dto) {
        log.debug("Login user for email: {}", dto.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new IllegalStateException("Authentication principal is not a User");
        }

        Long userId = Objects.requireNonNull(
                user.getId(),
                "Authenticated user id must not be null"
        );
        String jwt = jwtService.generateToken(userId);

        log.debug("User with email {} logged successfully", user.getEmail());
        return new UserJwtResponseDto(jwt);
    }

    /**
     * Retrieves the current authenticated user's information based on the provided user ID.
     *
     * @param id the ID of the user to retrieve information for, typically extracted from the JWT token
     * @return a UserMeResponseDto containing the current user's information if the user is found
     */
    public UserMeResponseDto getCurrentUser(Long id) {
        log.debug("Get current user for userId: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new IllegalArgumentException("User not found");
                });

        UserMeResponseDto response = modelMapper.map(user, UserMeResponseDto.class);

        log.debug("Current user for userId: {}", id);
        return response;
    }
}