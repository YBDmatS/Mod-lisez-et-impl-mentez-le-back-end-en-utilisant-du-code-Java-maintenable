package com.chatop.api.controller;

import com.chatop.api.dto.UserRegisterRequestDto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthController register endpoint integration tests")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthRegisterControllerIntegrationTest {

    public static final String REGISTER_PATH = "/api/auth/register";

    private UserRegisterRequestDto testDto;
    private String registerBody;

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

        testDto = new UserRegisterRequestDto(
                "Test UserDTO",
                "user@test.com",
                "P@ss124!Word");

        registerBody = objectMapper.writeValueAsString(testDto);
    }

    @Test
    void registerUser_shouldReturnOkAndToken_whenRequestIsValid() throws Exception {
        //Given
        long userCountBefore = userRepository.count();

        // When & Then
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User savedUser = userRepository.findByEmail(testDto.getEmail()).orElseThrow();

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore + 1);
        assertThat(savedUser.getName()).isEqualTo(testDto.getName());
        assertThat(savedUser.getEmail()).isEqualTo(testDto.getEmail());
        assertTrue(
                passwordEncoder.matches(testDto.getPassword(), savedUser.getPasswordHash()),
                "Password should be encoded in database"
        );
    }

    @Test
    void registerUser_shouldReturnConflict_whenEmailAlreadyExists() throws Exception {

        // Given
        User existingUser = new User();
        existingUser.setName("Test User");
        existingUser.setEmail(testDto.getEmail());
        existingUser.setPasswordHash(passwordEncoder.encode("AnotherP@ss124!Word"));
        userRepository.save(existingUser);

        long userCountBefore = userRepository.count();
        String passwordHashBefore = existingUser.getPasswordHash();

        // When & Then
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(containsString("A user with this email already exists")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);

        User persistedUser = userRepository.findByEmail(testDto.getEmail()).orElseThrow();

        assertThat(persistedUser.getName()).isEqualTo(existingUser.getName());
        assertThat(persistedUser.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(persistedUser.getPasswordHash()).isEqualTo(passwordHashBefore);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenPasswordIsInvalid() throws Exception {

        // Given
        testDto.setPassword("password");
        registerBody = objectMapper.writeValueAsString(testDto);
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {

        // Given
        testDto.setEmail("g@");
        registerBody = objectMapper.writeValueAsString(testDto);
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Email should be valid")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenNameIsBlank() throws Exception {

        // Given
        testDto.setName("");
        registerBody = objectMapper.writeValueAsString(testDto);
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Name is required")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenRequestBodyIsMissingRequiredFields() throws Exception {

        // Given
        registerBody = "{}";
        long userCountBefore = userRepository.count();

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message", containsString("Password is required")))
                .andExpect(jsonPath("$.message", containsString("Name is required")))
                .andExpect(jsonPath("$.message", containsString("Email is required")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        long userCountAfter = userRepository.count();
        assertThat(userCountAfter).isEqualTo(userCountBefore);
    }
}