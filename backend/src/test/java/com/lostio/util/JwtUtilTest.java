package com.lostio.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil class.
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "test-secret-key-for-testing-purposes-only-minimum-256-bits-required";
    private final Long testExpiration = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    void testGenerateToken() {
        String username = "test@example.com";
        String token = jwtUtil.generateToken(username);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String username = "test@example.com";
        String token = jwtUtil.generateToken(username);
        
        String extractedUsername = jwtUtil.extractUsername(token);
        
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken() {
        String username = "test@example.com";
        String token = jwtUtil.generateToken(username);
        
        Boolean isValid = jwtUtil.validateToken(token, username);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithWrongUsername() {
        String username = "test@example.com";
        String wrongUsername = "wrong@example.com";
        String token = jwtUtil.generateToken(username);
        
        Boolean isValid = jwtUtil.validateToken(token, wrongUsername);
        
        assertFalse(isValid);
    }

    @Test
    void testExtractExpiration() {
        String username = "test@example.com";
        String token = jwtUtil.generateToken(username);
        
        assertDoesNotThrow(() -> jwtUtil.extractExpiration(token));
        assertNotNull(jwtUtil.extractExpiration(token));
    }
}
