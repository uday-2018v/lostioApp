package com.lostio.repository;

import com.lostio.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User document operations.
 * Provides methods to interact with the users collection in MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Find a user by email address.
     * @param email The email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a user exists with the given email.
     * @param email The email address to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find a user by reset token.
     * @param resetToken The password reset token
     * @return Optional containing the user if found
     */
    Optional<User> findByResetToken(String resetToken);
}
