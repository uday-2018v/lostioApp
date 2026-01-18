package com.lostio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class LostioApplication {

    public static void main(String[] args) {
        SpringApplication.run(LostioApplication.class, args);
        System.out.println("🚀 LostioApp Backend is running on http://localhost:8080");
    }
}
