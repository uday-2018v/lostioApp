package com.lostio.service;

import com.lostio.dto.request.UserUpdateRequest;
import com.lostio.dto.response.UserResponse;
import com.lostio.entity.User;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.exception.UnauthorizedException;
import com.lostio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for user management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    /**
     * Get user by ID
     */
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return modelMapper.map(user, UserResponse.class);
    }
    
    /**
     * Get all users (for chat user list)
     */
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(user -> modelMapper.map(user, UserResponse.class))
            .collect(Collectors.toList());
    }
    
    /**
     * Update user profile
     */
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.debug("Updating user with ID: {}", id);
        
        // Check if current user is authorized to update this profile
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));
        
        if (!currentUser.getId().equals(id) && currentUser.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to update this profile");
        }
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Update fields if provided
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getId());
        
        return modelMapper.map(updatedUser, UserResponse.class);
    }
    
    /**
     * Get current user from security context
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
