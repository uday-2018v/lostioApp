package com.lostio.controller;

import com.lostio.dto.request.ForgotPasswordRequest;
import com.lostio.dto.request.LoginRequest;
import com.lostio.dto.request.RegisterRequest;
import com.lostio.dto.request.ResetPasswordRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.LoginResponse;
import com.lostio.dto.response.UserResponse;
import com.lostio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Register a new user.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", user),
                HttpStatus.CREATED
        );
    }
    
    /**
     * Login user.
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
    
    /**
     * Forgot password.
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Initiate password reset process")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String message = authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(message, message));
    }
    
    /**
     * Reset password.
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password with token")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String message = authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(message, message));
    }
    
    /**
     * Get current user profile.
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get authenticated user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserResponse user = authService.getCurrentUser(email);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved", user));
    }
}
