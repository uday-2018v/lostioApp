package com.lostio.service;

import com.lostio.dto.request.ForgotPasswordRequest;
import com.lostio.dto.request.LoginRequest;
import com.lostio.dto.request.RegisterRequest;
import com.lostio.dto.request.ResetPasswordRequest;
import com.lostio.dto.response.LoginResponse;
import com.lostio.dto.response.UserResponse;
import com.lostio.entity.User;
import com.lostio.exception.BadRequestException;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.UserRepository;
import com.lostio.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication and authorization operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    
    /**
     * Register a new user
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());
        user.setRole(User.Role.USER);
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        
        return modelMapper.map(savedUser, UserResponse.class);
    }
    
    /**
     * Authenticate user and generate JWT token
     */
    public LoginResponse login(LoginRequest request) {
        log.debug("Attempting login for user: {}", request.getEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        
        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
        
        // Generate JWT token
        String token = jwtUtil.generateToken(
            user.getEmail(),
            user.getId(),
            user.getRole().name()
        );
        
        log.info("User logged in successfully: {}", user.getEmail());
        
        return new LoginResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().name()
        );
    }
    
    /**
     * Get current authenticated user
     */
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return modelMapper.map(user, UserResponse.class);
    }
    
    /**
     * Initiate password reset (placeholder implementation)
     */
    public void forgotPassword(ForgotPasswordRequest request) {
        log.debug("Password reset requested for: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
        
        // In a real implementation, you would:
        // 1. Generate a reset token
        // 2. Save it with expiration time
        // 3. Send email with reset link
        
        log.info("Password reset email would be sent to: {}", user.getEmail());
    }
    
    /**
     * Reset password with token (placeholder implementation)
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.debug("Attempting password reset with token");
        
        // In a real implementation, you would:
        // 1. Validate the reset token
        // 2. Check if it's not expired
        // 3. Find user by token
        // 4. Update password
        
        // For now, this is a placeholder
        throw new BadRequestException("Password reset feature is not fully implemented");
    }
}
