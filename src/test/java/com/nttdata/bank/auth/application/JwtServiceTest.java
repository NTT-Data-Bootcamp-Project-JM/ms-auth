package com.nttdata.bank.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks that a token created by the service can be verified with the same secret.
 */
class JwtServiceTest {

    @Test
    void createTokenKeepsTheSubject() {
        String secret = "bank-bootcamp-demo-secret-change-me";
        JwtService jwtService = new JwtService(secret, 30);
        String token = jwtService.create("admin", List.of("ADMIN"));
        String subject = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        assertEquals("admin", subject);
    }
}
