package com.lostio.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDateTime;

/**
 * Message document representing a chat message between two users.
 * Stored in MongoDB 'messages' collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
@CompoundIndexes({
    @CompoundIndex(name = "sender_receiver_idx", def = "{'senderId': 1, 'receiverId': 1, 'timestamp': -1}"),
    @CompoundIndex(name = "receiver_sender_idx", def = "{'receiverId': 1, 'senderId': 1, 'timestamp': -1}")
})
public class Message {
    
    /**
     * Unique identifier for the message (MongoDB ObjectId)
     */
    @Id
    private String id;
    
    /**
     * ID of the user sending the message
     */
    @Indexed
    private String senderId;
    
    /**
     * Name of the sender
     */
    private String senderName;
    
    /**
     * ID of the user receiving the message
     */
    @Indexed
    private String receiverId;
    
    /**
     * Name of the receiver
     */
    private String receiverName;
    
    /**
     * Message content
     */
    private String message;
    
    /**
     * Timestamp when the message was sent
     */
    @Indexed
    private LocalDateTime timestamp;
    
    /**
     * Whether the message has been read by the receiver
     */
    private Boolean isRead;
    
    /**
     * Constructor for creating a new message
     */
    public Message(String senderId, String receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }
}
