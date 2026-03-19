package com.chatop.api.controller;

import com.chatop.api.dto.UserRegisterRequestDto;
import com.chatop.api.exception.UserAlreadyExistsException;
import com.chatop.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthContoller integration Tests")
@WebMvcTest(controllers = AuthController.class)
class AuthControllerIntegrationTest {

    public static final String REGISTER_PATH = "/api/auth/register";

    @MockitoBean
    private UserService userService;

    private UserRegisterRequestDto testDto;
    private String registerBody;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testDto = new UserRegisterRequestDto(
                "Test User",
                "user@test.com",
                "P@ss124!Word");

        registerBody = objectMapper.writeValueAsString(testDto);
    }

    @Test
    void registerUser_shouldReturnOk_whenRequestIsValid() throws Exception {

        // When & Then
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Then
        verify(userService).register(any(UserRegisterRequestDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_shouldReturnConflict_whenEmailAlreadyExists() throws Exception {

        // Given
        doThrow(new UserAlreadyExistsException("Email already exists"))
                .when(userService)
                .register(any(UserRegisterRequestDto.class));

        // When & Then
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(containsString("A user with this email already exists")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists());

        // Then
        verify(userService).register(any(UserRegisterRequestDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenPasswordIsInvalid() throws Exception {

        // Given
        testDto.setPassword("password");
        registerBody = objectMapper.writeValueAsString(testDto);

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists());

        // Then
        verify(userService, never()).register(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {

        // Given
        testDto.setEmail("g@");
        registerBody = objectMapper.writeValueAsString(testDto);

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Email should be valid")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists());

        // Then
        verify(userService, never()).register(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenNameIsBlank() throws Exception {

        // Given
        testDto.setName("");
        registerBody = objectMapper.writeValueAsString(testDto);

        // When
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(containsString("Name is required")))
                .andExpect(jsonPath("$.path").value(REGISTER_PATH))
                .andExpect(jsonPath("$.timestamp").exists());

        // Then
        verify(userService, never()).register(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenRequestBodyIsMissingRequiredFields() throws Exception {

        // Given
        registerBody = "{}";

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
                .andExpect(jsonPath("$.timestamp").exists());

        // Then
        verify(userService, never()).register(any());
        verifyNoMoreInteractions(userService);
    }
}