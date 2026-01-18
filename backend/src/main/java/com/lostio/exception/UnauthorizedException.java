package com.lostio.exception;

/**
 * Exception thrown when an unauthorized action is attempted
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
