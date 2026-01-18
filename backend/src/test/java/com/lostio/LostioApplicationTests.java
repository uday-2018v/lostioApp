package com.lostio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test for LostioApplication.
 * Tests that the Spring Boot application context loads successfully.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/lostioapp_test",
    "jwt.secret=test-secret-key-for-testing-purposes-only",
    "jwt.expiration=86400000",
    "cloudinary.cloud-name=",
    "cloudinary.api-key=",
    "cloudinary.api-secret="
})
class LostioApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // If the context fails to load, this test will fail
    }
}
