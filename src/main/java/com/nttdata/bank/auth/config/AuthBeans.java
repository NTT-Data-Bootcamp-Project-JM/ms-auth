package com.nttdata.bank.auth.config;

import com.nttdata.bank.auth.domain.model.UserDocument;
import com.nttdata.bank.auth.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Password hashing and the demo administrator used during the review.
 */
@Configuration
public class AuthBeans {

    /**
     * @return bcrypt encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the configured administrator the first time the database is empty.
     *
     * @param users login store
     * @param passwordEncoder password hasher
     * @param username bootstrap username
     * @param password bootstrap password
     * @return startup runner
     */
    @Bean
    public CommandLineRunner bootstrapAdmin(UserRepository users,
                                            PasswordEncoder passwordEncoder,
                                            @Value("${bank.security.bootstrap-username}") String username,
                                            @Value("${bank.security.bootstrap-password}") String password) {
        return args -> {
            if (users.findByUsername(username).isEmpty()) {
                UserDocument admin = new UserDocument();
                admin.setUsername(username);
                admin.setPasswordHash(passwordEncoder.encode(password));
                admin.setRoles(List.of("ADMIN"));
                users.save(admin);
            }
        };
    }

    }
