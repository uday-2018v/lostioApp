package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for conversation response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private String userId;
    private String userName;
    private String userAvatar;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private long unreadCount;
}
