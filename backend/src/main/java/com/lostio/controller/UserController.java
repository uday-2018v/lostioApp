package com.lostio.controller;

import com.lostio.dto.request.UserUpdateRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.UserResponse;
import com.lostio.service.FileUploadService;
import com.lostio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for user management endpoints.
 */
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User management APIs")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    /**
     * Get user by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    /**
     * Update user profile.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Update user information")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }
    
    /**
     * Get all users.
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users (for chat user list)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserResponse> users = userService.getAllUsers(pageable);
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users.getContent()));
        } else {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        }
    }
    
    /**
     * Upload user avatar.
     */
    @PostMapping("/{id}/avatar")
    @Operation(summary = "Upload user avatar", description = "Upload profile picture for user")
    public ResponseEntity<ApiResponse<UserResponse>> uploadAvatar(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        String avatarUrl = fileUploadService.uploadAvatar(file, id);
        UserResponse user = userService.updateAvatar(id, avatarUrl);
        
        return ResponseEntity.ok(ApiResponse.success("Avatar uploaded successfully", user));
    }
}
