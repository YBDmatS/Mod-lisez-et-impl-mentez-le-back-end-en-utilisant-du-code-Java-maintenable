package com.chatop.api.service;

import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("UserService unit tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserService userService;
    private UserRegisterRequestDto testDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("user@test.com");

        testDto = new UserRegisterRequestDto(
                "Test User",
                "user@test.com",
                "P@ss124!Word");
    }

    @Test
    void register_shouldSaveUser_whenDtoIsValid() {

        // Given
        String encodedPassword = "encoded-password";
        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(false);
        when(modelMapper.map(testDto, User.class)).thenReturn(testUser);
        when(passwordEncoder.encode(testDto.getPassword())).thenReturn(encodedPassword);

        // When
        userService.register(testDto);

        // Then
        verify(userRepository).existsByEmail(testDto.getEmail());
        verify(modelMapper).map(testDto, User.class);
        verify(passwordEncoder).encode(testDto.getPassword());
        verify(userRepository).save(testUser);
        assertEquals(encodedPassword, testUser.getPasswordHash());
    }

    @Test
    void register_shouldThrowUserAlreadyExistsException_whenEmailAlreadyUsed() {

        // Given
        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(testDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Email already used");

        // Then
        verify(userRepository).existsByEmail(testDto.getEmail());
        verify(modelMapper, never()).map(any(), eq(User.class));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowDataIntegrityViolationException_whenSaveFails() {

        // Given
        String encodedPassword = "encoded-password";
        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(false);
        when(modelMapper.map(testDto, User.class)).thenReturn(testUser);
        when(passwordEncoder.encode(testDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(testUser)).thenThrow(new DataIntegrityViolationException("Unexpected error"));

        // When & Then
        assertThatThrownBy(() -> userService.register(testDto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Unexpected error");

        // Then
        verify(userRepository).existsByEmail(testDto.getEmail());
        verify(modelMapper).map(testDto, User.class);
        verify(passwordEncoder).encode(testDto.getPassword());
        verify(userRepository).save(testUser);
    }
}