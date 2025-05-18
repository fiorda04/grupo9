package com.oo2.grupo9.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestionDeTicketsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Gestión de Tickets")
                        .description("API para la gestión de tickets de soporte")
                        .version("v1.0"));
    }
}
