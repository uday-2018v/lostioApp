package com.lostio.repository;

import com.lostio.document.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Message document operations.
 * Provides methods to interact with the messages collection in MongoDB.
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    /**
     * Find all messages between two users (bidirectional).
     * @param senderId First user ID
     * @param receiverId Second user ID
     * @param pageable Pagination information
     * @return Page of messages between the two users
     */
    @Query("{'$or': [" +
           "{'senderId': ?0, 'receiverId': ?1}, " +
           "{'senderId': ?1, 'receiverId': ?0}" +
           "]}")
    Page<Message> findMessagesBetweenUsers(String senderId, String receiverId, Pageable pageable);
    
    /**
     * Find all conversations for a user (distinct users they've chatted with).
     * @param userId The user ID
     * @return List of messages (one per conversation)
     */
    @Query("{'$or': [{'senderId': ?0}, {'receiverId': ?0}]}")
    List<Message> findConversationsForUser(String userId);
    
    /**
     * Find unread messages for a user.
     * @param receiverId The receiver's user ID
     * @return List of unread messages
     */
    List<Message> findByReceiverIdAndIsReadFalse(String receiverId);
    
    /**
     * Count unread messages for a user.
     * @param receiverId The receiver's user ID
     * @return Number of unread messages
     */
    long countByReceiverIdAndIsReadFalse(String receiverId);
    
    /**
     * Find messages sent by a specific user.
     * @param senderId The sender's user ID
     * @param pageable Pagination information
     * @return Page of messages sent by the user
     */
    Page<Message> findBySenderId(String senderId, Pageable pageable);
    
    /**
     * Find messages received by a specific user.
     * @param receiverId The receiver's user ID
     * @param pageable Pagination information
     * @return Page of messages received by the user
     */
    Page<Message> findByReceiverId(String receiverId, Pageable pageable);
}
