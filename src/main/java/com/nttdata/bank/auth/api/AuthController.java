package com.nttdata.bank.auth.api;

import com.nttdata.bank.auth.api.AuthDtos.LoginRequest;
import com.nttdata.bank.auth.api.AuthDtos.RegisterRequest;
import com.nttdata.bank.auth.api.AuthDtos.TokenResponse;
import com.nttdata.bank.auth.application.AuthService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public authentication API. The gateway leaves this path open.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * @param authService authentication use cases
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a login.
     *
     * @param request username, password and optional roles
     * @return created token
     */
    @PostMapping("/register")
    public Single<ResponseEntity<TokenResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request).map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body));
    }

    /**
     * Exchanges credentials for a JWT.
     *
     * @param request username and password
     * @return bearer token
     */
    @PostMapping("/login")
    public Single<ResponseEntity<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request).map(ResponseEntity::ok);
    }
}
