package com.lostio.repository;

import com.lostio.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Message entity operations.
 * Provides database access methods for chat messaging.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Find all messages between two users
     * @param senderId the sender's user ID
     * @param receiverId the receiver's user ID
     * @return list of messages ordered by timestamp
     */
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderId = :senderId AND m.receiverId = :receiverId) OR " +
           "(m.senderId = :receiverId AND m.receiverId = :senderId) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(
        @Param("senderId") Long senderId, 
        @Param("receiverId") Long receiverId
    );
    
    /**
     * Find all unique conversation partners for a user
     * @param userId the user's ID
     * @return list of user IDs who have conversed with this user
     */
    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END " +
           "FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId")
    List<Long> findConversationPartners(@Param("userId") Long userId);
}
