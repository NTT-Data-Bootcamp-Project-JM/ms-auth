package com.nttdata.bank.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Issues JWT access tokens. Bank customers and Yanki users are owned by their own services.
 */
@SpringBootApplication
public class AuthApplication {

    /**
     * Starts the authentication service.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
