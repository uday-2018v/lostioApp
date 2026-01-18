package com.lostio.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standardized error response format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    
    public ErrorResponse(String message, String error) {
        this.success = false;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
