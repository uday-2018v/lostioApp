package com.lostio;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for LostioApp backend.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "LostioApp API",
                version = "1.0.0",
                description = "REST API for Lost and Found mobile application",
                contact = @Contact(
                        name = "LostioApp Team",
                        email = "support@lostioapp.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class LostioApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LostioApplication.class, args);
    }
}
