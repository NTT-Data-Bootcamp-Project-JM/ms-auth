package com.nttdata.bank.auth.service;

import com.nttdata.bank.auth.api.dto.LoginRequest;
import com.nttdata.bank.auth.api.dto.RegisterRequest;
import com.nttdata.bank.auth.api.dto.TokenResponse;
import com.nttdata.bank.auth.domain.model.UserDocument;
import com.nttdata.bank.auth.domain.repository.UserRepository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Registers logins and exchanges a password for a JWT.
 */
@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * @param users login store
     * @param passwordEncoder bcrypt encoder
     * @param jwtService token signer
     */
    public AuthService(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Creates a login when the username is still free.
     *
     * @param request registration data
     * @return token for the new login
     */
    public Single<TokenResponse> register(RegisterRequest request) {
        return Single.fromCallable(() -> {
            if (users.findByUsername(request.username()).isPresent()) {
                throw new IllegalArgumentException("Username is already registered");
            }
            UserDocument user = new UserDocument();
            user.setUsername(request.username());
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            List<String> roles = request.roles() == null || request.roles().isEmpty()
                    ? List.of("CUSTOMER") : request.roles();
            user.setRoles(roles);
            users.save(user);
            return token(user);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Checks the password and returns a token.
     *
     * @param request credentials
     * @return signed token
     */
    public Single<TokenResponse> login(LoginRequest request) {
        return Single.fromCallable(() -> {
            UserDocument user = users.findByUsername(request.username())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
            if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid username or password");
            }
            return token(user);
        }).subscribeOn(Schedulers.io());
    }

    private TokenResponse token(UserDocument user) {
        return new TokenResponse(jwtService.create(user.getUsername(), user.getRoles()), "Bearer", jwtService.expirationMinutes());
    }
}
