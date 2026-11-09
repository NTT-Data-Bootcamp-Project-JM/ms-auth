package com.nttdata.bank.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credentials presented to obtain a token.
 *
 * @param username login name
 * @param password plain password
 */
public record LoginRequest(@NotBlank String username, @NotBlank String password) {
}