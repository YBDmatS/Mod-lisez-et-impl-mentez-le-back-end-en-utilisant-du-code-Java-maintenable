package com.chatop.api.service;

import com.chatop.api.dto.UserJwtResponseDto;
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
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService userService;
    private UserRegisterRequestDto testDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("user@test.com");

        testDto = new UserRegisterRequestDto(
                "Test User",
                "user@test.com",
                "P@ss124!Word");
    }

    @Test
    void register_shouldSaveUserAndReturnJwt_whenDtoIsValid() {

        // Given
        String encodedPassword = "encoded-password";
        String expectedJwt = "jwt";

        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(false);
        when(modelMapper.map(testDto, User.class)).thenReturn(testUser);
        when(passwordEncoder.encode(testDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(jwtService.generateToken(testUser.getId())).thenReturn(expectedJwt);

        // When
        UserJwtResponseDto result = userService.register(testDto);

        // Then
        verify(userRepository).existsByEmail(testDto.getEmail());
        verify(modelMapper).map(testDto, User.class);
        verify(passwordEncoder).encode(testDto.getPassword());
        verify(userRepository).save(testUser);
        verify(jwtService).generateToken(testUser.getId());

        assertEquals(encodedPassword, testUser.getPasswordHash());
        assertEquals(expectedJwt, result.getToken());
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
        verify(jwtService, never()).generateToken(anyLong());
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
        verify(jwtService, never()).generateToken(anyLong());
    }

    @Test
    void register_shouldThrowNullPointerException_whenEncodedPasswordIsNull() {
        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(false);
        when(modelMapper.map(testDto, User.class)).thenReturn(testUser);
        when(passwordEncoder.encode(testDto.getPassword())).thenReturn(null);

        assertThatThrownBy(() -> userService.register(testDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Encoded password must not be null");

        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyLong());
    }

    @Test
    void register_shouldThrowNullPointerException_whenSavedUserIdIsNull() {
        User savedUser = new User();
        savedUser.setName("Test User");
        savedUser.setEmail("user@test.com");
        savedUser.setPasswordHash("encoded-password");

        when(userRepository.existsByEmail(testDto.getEmail())).thenReturn(false);
        when(modelMapper.map(testDto, User.class)).thenReturn(testUser);
        when(passwordEncoder.encode(testDto.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(testUser)).thenReturn(savedUser);

        assertThatThrownBy(() -> userService.register(testDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Saved user id must not be null");

        verify(jwtService, never()).generateToken(anyLong());
    }
}