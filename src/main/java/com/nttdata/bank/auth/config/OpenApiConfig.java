package com.nttdata.bank.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Published OpenAPI contract of the authentication service.
 */
@Configuration
public class OpenApiConfig {

    /**
     * @return contract metadata
     */
    @Bean
    public OpenAPI authOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Authentication")
                .version("1.0.0")
                .description("Registers users and issues the bearer token used by the other services."));
    }
}
