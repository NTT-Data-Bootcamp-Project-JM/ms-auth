package com.nttdata.bank.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

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