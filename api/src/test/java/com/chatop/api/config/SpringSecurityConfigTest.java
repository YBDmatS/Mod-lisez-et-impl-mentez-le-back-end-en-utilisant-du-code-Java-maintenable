package com.chatop.api.config;

import com.chatop.api.controller.AuthController;
import com.chatop.api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Security configuration tests")
@WebMvcTest(controllers = AuthController.class)
@Import(SpringSecurityConfig.class)
class SpringSecurityConfigTest {

    public static final String REGISTER_PATH = "/api/auth/register";
    @MockitoBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_shouldBeAccessibleWithoutAuthentication() throws Exception {

        String body = """
                {
                  "name": "Test",
                  "email": "test@test.com",
                  "password": "P@ssword123!"
                }
                """;

        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorized_whenNoAuthentication() throws Exception {

        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isForbidden());
    }
}