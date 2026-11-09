package com.nttdata.bank.auth.api.dto;

/**
 * JWT returned to the caller.
 *
 * @param accessToken signed token
 * @param tokenType always Bearer
 * @param expiresInMinutes configured lifetime
 */
public record TokenResponse(String accessToken, String tokenType, long expiresInMinutes) {
}