package com.nttdata.bank.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Creates HMAC JWT tokens shared with the gateway and the business services.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMinutes;

    /**
     * Reads the shared secret and lifetime from configuration.
     *
     * @param secret HMAC secret of at least 32 bytes
     * @param expirationMinutes token lifetime
     */
    public JwtService(@Value("${bank.security.jwt-secret}") String secret,
                      @Value("${bank.security.jwt-expiration-minutes}") long expirationMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * Signs a token for the given subject and roles.
     *
     * @param subject username
     * @param roles granted roles
     * @return compact JWT
     */
    public String create(String subject, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * @return configured lifetime in minutes
     */
    public long expirationMinutes() {
        return expirationMinutes;
    }
}
