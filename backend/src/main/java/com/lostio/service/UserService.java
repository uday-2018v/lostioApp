package com.lostio.service;

import com.lostio.document.User;
import com.lostio.dto.request.UserUpdateRequest;
import com.lostio.dto.response.UserResponse;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 */
@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Get user by ID.
     * @param userId User ID
     * @return User response
     */
    public UserResponse getUserById(String userId) {
        logger.info("Fetching user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return modelMapper.map(user, UserResponse.class);
    }
    
    /**
     * Update user profile.
     * @param userId User ID
     * @param request Update request
     * @return Updated user response
     */
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        logger.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
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
        
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        
        logger.info("User updated successfully: {}", userId);
        return modelMapper.map(user, UserResponse.class);
    }
    
    /**
     * Get all users (for chat user list).
     * @param pageable Pagination parameters
     * @return Page of user responses
     */
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        logger.info("Fetching all users");
        Page<User> users = userRepository.findAll(pageable);
        
        return users.map(user -> modelMapper.map(user, UserResponse.class));
    }
    
    /**
     * Get all users without pagination.
     * @return List of user responses
     */
    public List<UserResponse> getAllUsers() {
        logger.info("Fetching all users without pagination");
        List<User> users = userRepository.findAll();
        
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Update user avatar.
     * @param userId User ID
     * @param avatarUrl Avatar URL
     * @return Updated user response
     */
    public UserResponse updateAvatar(String userId, String avatarUrl) {
        logger.info("Updating avatar for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setAvatarUrl(avatarUrl);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        
        logger.info("Avatar updated successfully for user: {}", userId);
        return modelMapper.map(user, UserResponse.class);
    }
}
