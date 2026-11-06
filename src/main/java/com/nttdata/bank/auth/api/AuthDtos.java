package com.nttdata.bank.auth.api;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Authentication request and response bodies.
 */
public final class AuthDtos {

    private AuthDtos() {
    }

    /**
     * Data required to open a login.
     *
     * @param username unique login name
     * @param password plain password, stored only as a hash
     * @param roles optional role names; CUSTOMER is used when omitted
     */
    public record RegisterRequest(
            @NotBlank String username,
            @NotBlank String password,
            List<String> roles
    ) {
    }

    /**
     * Credentials presented to obtain a token.
     *
     * @param username login name
     * @param password plain password
     */
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    /**
     * JWT returned to the caller.
     *
     * @param accessToken signed token
     * @param tokenType always Bearer
     * @param expiresInMinutes configured lifetime
     */
    public record TokenResponse(String accessToken, String tokenType, long expiresInMinutes) {
    }
}
