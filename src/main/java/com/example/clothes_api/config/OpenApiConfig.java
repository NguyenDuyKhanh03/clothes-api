package com.example.clothes_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Clothes API",
                version = "1.0",
                description = "OpenApi documentation for Clothes API",
                contact = @Contact(
                        name = "Duy Khanh",
                        email = "duykhanh2707.work@gmail.com"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "Local ENV"
        )

)
@SecurityScheme(
        name="bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
