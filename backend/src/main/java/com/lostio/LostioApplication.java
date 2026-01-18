package com.lostio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for LostioApp backend
 * 
 * This is the entry point for the Spring Boot application.
 * It provides a REST API for the Lost and Found mobile application.
 * 
 * Features:
 * - JWT-based authentication and authorization
 * - User management
 * - Lost/found item reporting
 * - Claims management
 * - Real-time chat messaging
 * - File upload with Cloudinary
 * - API documentation with Swagger/OpenAPI
 */
@SpringBootApplication
@EnableJpaAuditing
public class LostioApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LostioApplication.class, args);
    }
}
