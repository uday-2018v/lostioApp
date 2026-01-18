package com.lostio.util;

import com.lostio.entity.User;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for user-related operations
 */
@Component
@RequiredArgsConstructor
public class UserUtil {
    
    private final UserRepository userRepository;
    
    /**
     * Get currently authenticated user from security context
     * @return the authenticated user
     * @throws ResourceNotFoundException if user not found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
