package com.lostio.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User document representing a user in the LostioApp system.
 * Stored in MongoDB 'users' collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    /**
     * Unique identifier for the user (MongoDB ObjectId)
     */
    @Id
    private String id;
    
    /**
     * User's email address (unique)
     */
    @Indexed(unique = true)
    private String email;
    
    /**
     * User's encrypted password
     */
    private String password;
    
    /**
     * User's full name
     */
    private String name;
    
    /**
     * User's phone number
     */
    private String phone;
    
    /**
     * User's location
     */
    private String location;
    
    /**
     * URL to user's avatar image
     */
    private String avatarUrl;
    
    /**
     * User's roles (e.g., USER, ADMIN)
     */
    private Set<String> roles = new HashSet<>();
    
    /**
     * Password reset token (used for forgot password flow)
     */
    private String resetToken;
    
    /**
     * Reset token expiration time
     */
    private LocalDateTime resetTokenExpiry;
    
    /**
     * Timestamp when the user was created
     */
    @CreatedDate
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the user was last updated
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for creating a new user with basic information
     */
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = new HashSet<>();
        this.roles.add("USER");
    }
}
