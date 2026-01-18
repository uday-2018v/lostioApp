package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for message response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    
    private Long id;
    
    private Long senderId;
    
    private Long receiverId;
    
    private String message;
    
    private LocalDateTime timestamp;
    
    private Boolean isRead;
}
