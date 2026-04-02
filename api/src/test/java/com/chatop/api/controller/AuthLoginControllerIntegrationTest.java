package com.chatop.api.controller;

import com.chatop.api.dto.UserLoginRequestDto;
import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthController login endpoint integration tests")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthLoginControllerIntegrationTest {

    public static final String LOGIN_PATH = "/api/auth/login";

    private UserLoginRequestDto loginRequest;
    private String loginBody;
    private User existingUser;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        loginRequest = new UserLoginRequestDto(
                "user@test.com",
                "P@ss124!Word"
        );
        loginBody = objectMapper.writeValueAsString(loginRequest);

        existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail(loginRequest.getEmail());
        existingUser.setPasswordHash(passwordEncoder.encode(loginRequest.getPassword()));
        userRepository.save(existingUser);
    }

    @Test
    void login_shouldReturnOkAndToken_whenCredentialsAreValid() throws Exception {
        //Given
        long userCountBefore = userRepository.count();

        // When & Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        User currentUser = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
        assertThat(currentUser.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(currentUser.getPasswordHash()).isEqualTo(existingUser.getPasswordHash());
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() throws Exception {

        // Given
        long userCountBefore = userRepository.count();
        loginRequest.setPassword("WrongP@ss124!Word");
        loginBody = objectMapper.writeValueAsString(loginRequest);

        // When & Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value(containsString("Invalid credentials")))
                .andExpect(jsonPath("$.path").value(LOGIN_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void login_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {

        // Given
        loginBody = "{}";
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Email is required")))
                .andExpect(jsonPath("$.message").value(containsString("Password is required")))
                .andExpect(jsonPath("$.path").value(LOGIN_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void login_shouldNotExposePasswordInResponse() throws Exception {

        // Given
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(content().string(not(loginRequest.getPassword())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void login_shouldReturnUnauthorized_whenUserNotFound() throws Exception {

        // Given
        loginRequest.setEmail("unknown@mail.com");
        loginBody = objectMapper.writeValueAsString(loginRequest);
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value(containsString("Invalid credentials")))
                .andExpect(jsonPath("$.path").value(LOGIN_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

}