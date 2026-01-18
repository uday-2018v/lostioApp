package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    
    private String email;
    
    private String name;
    
    private String phone;
    
    private String location;
    
    private String avatarUrl;
    
    private String role;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
