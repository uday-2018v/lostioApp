package com.lostio.service;

import com.lostio.document.User;
import com.lostio.dto.request.ForgotPasswordRequest;
import com.lostio.dto.request.LoginRequest;
import com.lostio.dto.request.RegisterRequest;
import com.lostio.dto.request.ResetPasswordRequest;
import com.lostio.dto.response.LoginResponse;
import com.lostio.dto.response.UserResponse;
import com.lostio.exception.BadRequestException;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.UserRepository;
import com.lostio.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

/**
 * Service for authentication operations (register, login, password reset).
 */
@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Register a new user.
     * @param registerRequest Registration request
     * @return User response
     */
    public UserResponse register(RegisterRequest registerRequest) {
        logger.info("Registering new user with email: {}", registerRequest.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setPhone(registerRequest.getPhone());
        user.setLocation(registerRequest.getLocation());
        user.setRoles(new HashSet<>());
        user.getRoles().add("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        user = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", user.getId());
        
        return modelMapper.map(user, UserResponse.class);
    }
    
    /**
     * Login user and generate JWT token.
     * @param loginRequest Login request
     * @return Login response with token
     */
    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("User attempting login: {}", loginRequest.getEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        
        // Generate JWT token
        String token = jwtUtil.generateToken(loginRequest.getEmail());
        
        // Get user details
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));
        
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        
        logger.info("User logged in successfully: {}", loginRequest.getEmail());
        return new LoginResponse(token, userResponse);
    }
    
    /**
     * Initiate forgot password flow.
     * @param request Forgot password request
     * @return Success message
     */
    public String forgotPassword(ForgotPasswordRequest request) {
        logger.info("Forgot password request for email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
        
        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        
        // In production, send email with reset link
        logger.info("Password reset token generated for user: {}", request.getEmail());
        logger.info("Reset token: {}", resetToken);
        
        return "Password reset instructions sent to your email";
    }
    
    /**
     * Reset password with token.
     * @param request Reset password request
     * @return Success message
     */
    public String resetPassword(ResetPasswordRequest request) {
        logger.info("Password reset attempt with token");
        
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));
        
        // Check if token is expired
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        logger.info("Password reset successfully for user: {}", user.getEmail());
        return "Password reset successfully";
    }
    
    /**
     * Get current user profile.
     * @param email User's email
     * @return User response
     */
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return modelMapper.map(user, UserResponse.class);
    }
}
