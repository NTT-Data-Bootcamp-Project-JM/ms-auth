package com.nttdata.bank.auth.api.controller;

import com.nttdata.bank.auth.api.dto.LoginRequest;
import com.nttdata.bank.auth.api.dto.RegisterRequest;
import com.nttdata.bank.auth.api.dto.TokenResponse;
import com.nttdata.bank.auth.api.contract.AuthApi;
import com.nttdata.bank.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements the authentication contract generated from {@code openapi/openapi.yaml}.
 */
@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;

    /**
     * @param authService authentication use cases
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TokenResponse> register(RegisterRequest request) {
        return authService.register(request)
                .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body))
                .blockingGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        return authService.login(request)
                .map(ResponseEntity::ok)
                .blockingGet();
    }
}
