package com.nttdata.bank.auth.application;

import com.nttdata.bank.auth.api.AuthDtos.LoginRequest;
import com.nttdata.bank.auth.api.AuthDtos.RegisterRequest;
import com.nttdata.bank.auth.api.AuthDtos.TokenResponse;
import com.nttdata.bank.auth.domain.UserDocument;
import com.nttdata.bank.auth.domain.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Auth use cases with a mocked user store and a real token signer.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    private static final String SECRET = "bank-bootcamp-demo-secret-change-me";

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(users, passwordEncoder, new JwtService(SECRET, 120));
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(users.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void registerSignsACustomerTokenWhenNoRolesAreGiven() {
        when(users.findByUsername("ana")).thenReturn(Optional.empty());

        TokenResponse token = service.register(new RegisterRequest("ana", "secret", List.of())).blockingGet();

        assertEquals("Bearer", token.tokenType());
        assertEquals(120, token.expiresInMinutes());
        assertEquals("ana", subject(token.accessToken()));
        verify(users).save(any(UserDocument.class));
    }

    @Test
    void registerKeepsTheRequestedRoles() {
        when(users.findByUsername("admin")).thenReturn(Optional.empty());

        TokenResponse token = service.register(new RegisterRequest("admin", "secret", List.of("ADMIN"))).blockingGet();

        assertEquals("admin", subject(token.accessToken()));
    }

    @Test
    void registerRejectsADuplicateUsername() {
        when(users.findByUsername("ana")).thenReturn(Optional.of(new UserDocument()));

        assertThrows(IllegalArgumentException.class,
                () -> service.register(new RegisterRequest("ana", "secret", null)).blockingGet());
    }

    @Test
    void loginReturnsATokenWhenThePasswordMatches() {
        UserDocument user = new UserDocument();
        user.setUsername("ana");
        user.setPasswordHash("hash");
        user.setRoles(List.of("CUSTOMER"));
        when(users.findByUsername("ana")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hash")).thenReturn(true);

        TokenResponse token = service.login(new LoginRequest("ana", "secret")).blockingGet();

        assertEquals("ana", subject(token.accessToken()));
    }

    @Test
    void loginRejectsAnUnknownUser() {
        when(users.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.login(new LoginRequest("missing", "secret")).blockingGet());
    }

    @Test
    void loginRejectsAWrongPassword() {
        UserDocument user = new UserDocument();
        user.setUsername("ana");
        user.setPasswordHash("hash");
        user.setRoles(List.of("CUSTOMER"));
        when(users.findByUsername("ana")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.login(new LoginRequest("ana", "wrong")).blockingGet());
    }

    private String subject(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
