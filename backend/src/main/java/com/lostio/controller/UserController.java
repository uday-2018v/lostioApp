package com.lostio.controller;

import com.lostio.dto.request.UserUpdateRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.FileUploadResponse;
import com.lostio.dto.response.UserResponse;
import com.lostio.service.FileUploadService;
import com.lostio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for user management endpoints
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
    
    private final UserService userService;
    private final FileUploadService fileUploadService;
    
    /**
     * Get all users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Get list of all users (for chat)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    /**
     * Update user profile
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user profile information")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }
    
    /**
     * Upload user avatar
     */
    @PostMapping("/{id}/avatar")
    @Operation(summary = "Upload avatar", description = "Upload user avatar image")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse uploadResponse = fileUploadService.uploadImage(file, "avatars");
        
        // Update user avatar URL
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setAvatarUrl(uploadResponse.getUrl());
        userService.updateUser(id, updateRequest);
        
        return ResponseEntity.ok(
            ApiResponse.success("Avatar uploaded successfully", uploadResponse));
    }
}
