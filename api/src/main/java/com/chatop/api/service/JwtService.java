package com.chatop.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service for generating JSON Web Tokens (JWT) for authenticated users.
 * This service uses the JwtEncoder to create JWTs with specific claims such as issuer, issued at, expiration, and subject (user ID).
 * The generated JWT can be used for authenticating subsequent requests to the API.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    /**
     * Generates a JWT token for the given user ID.
     * The token includes claims such as issuer, issued at time, expiration time (set to 1 day), and subject (the user ID).
     *
     * @param userId the ID of the user for whom the token is being generated
     * @return a JWT token as a String that can be used for authentication
     */
    public String generateToken(Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userId.toString())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
