package com.chatop.api.service;

import com.chatop.api.dto.UserJwtResponseDto;
import com.chatop.api.dto.UserLoginRequestDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("User login service unit tests")
@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private UserService userService;
    private UserLoginRequestDto loginRequest;
    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setName("Test User");
        authenticatedUser.setEmail("user@test.com");

        loginRequest = new UserLoginRequestDto(
                "user@test.com",
                "P@ss124!Word");
    }

    @Test
    void login_shouldAuthenticateUserAndReturnJwt_whenCredentialsAreValid() {

        // Given
        String expectedJwt = "jwt";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser.getId())).thenReturn(expectedJwt);

        // When
        UserJwtResponseDto result = userService.login(loginRequest);

        // Then
        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && loginRequest.getEmail().equals(auth.getPrincipal())
                        && loginRequest.getPassword().equals(auth.getCredentials())
        ));
        verify(authentication).getPrincipal();
        verify(jwtService).generateToken(authenticatedUser.getId());

        assertEquals(expectedJwt, result.getToken());
        assertEquals("user@test.com", authenticatedUser.getEmail());
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenAuthenticationFails() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");

        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && loginRequest.getEmail().equals(auth.getPrincipal())
                        && loginRequest.getPassword().equals(auth.getCredentials())
        ));
        verify(jwtService, never()).generateToken(anyLong());
    }

    @Test
    void login_shouldThrowIllegalStateException_whenAuthenticationPrincipalIsNotUser() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("not-a-user");

        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(IllegalStateException.class);

        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && loginRequest.getEmail().equals(auth.getPrincipal())
                        && loginRequest.getPassword().equals(auth.getCredentials())
        ));
        verify(authentication).getPrincipal();
        verify(jwtService, never()).generateToken(anyLong());
    }

    @Test
    void login_shouldThrowNullPointerException_whenAuthenticatedUserIdIsNull() {
        // Given
        authenticatedUser.setId(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Authenticated user id must not be null");

        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && loginRequest.getEmail().equals(auth.getPrincipal())
                        && loginRequest.getPassword().equals(auth.getCredentials())
        ));
        verify(authentication).getPrincipal();
        verify(jwtService, never()).generateToken(anyLong());
    }

    @Test
    void login_shouldNotEncodePasswordOrAccessRepository_whenAuthenticating() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser.getId())).thenReturn("jwt-token");

        // When
        userService.login(loginRequest);

        // Then
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), any());
    }
}