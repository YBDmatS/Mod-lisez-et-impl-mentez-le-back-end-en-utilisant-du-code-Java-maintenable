package com.chatop.api.config;

import com.chatop.api.config.properties.FrontProperties;
import com.chatop.api.config.properties.JwtProperties;
import com.chatop.api.security.CustomAccessDeniedHandler;
import com.chatop.api.security.CustomAuthenticationEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@DisplayName("SpringSecurityConfig BearerTokenResolver unit tests")
class SpringSecurityConfigBearerTokenResolverTest {

    private BearerTokenResolver bearerTokenResolver;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = mock(JwtProperties.class);
        FrontProperties frontProperties = mock(FrontProperties.class);
        CustomAccessDeniedHandler customAccessDeniedHandler = mock(CustomAccessDeniedHandler.class);
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint = mock(CustomAuthenticationEntryPoint.class);
        SpringSecurityConfig config = new SpringSecurityConfig(jwtProperties, frontProperties, customAuthenticationEntryPoint, customAccessDeniedHandler);
        bearerTokenResolver = config.bearerTokenResolver();
    }

    @Test
    void bearerTokenResolver_shouldReturnNull_forRegisterEndpoint() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/register");
        request.addHeader("Authorization", "Bearer test-token");

        String result = bearerTokenResolver.resolve(request);

        assertNull(result);
    }

    @Test
    void bearerTokenResolver_shouldReturnNull_forLoginEndpoint() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/login");
        request.addHeader("Authorization", "Bearer test-token");

        String result = bearerTokenResolver.resolve(request);

        assertNull(result);
    }

    @Test
    void bearerTokenResolver_shouldResolveToken_forProtectedEndpoint() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/rentals");
        request.addHeader("Authorization", "Bearer test-token");

        String result = bearerTokenResolver.resolve(request);

        assertEquals("test-token", result);
    }
}