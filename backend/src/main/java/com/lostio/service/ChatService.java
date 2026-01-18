package com.lostio.service;

import com.lostio.dto.request.MessageRequest;
import com.lostio.dto.response.MessageResponse;
import com.lostio.entity.Message;
import com.lostio.entity.User;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.MessageRepository;
import com.lostio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for chat and messaging operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    /**
     * Send a message
     */
    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        log.debug("Sending message from current user to user: {}", request.getReceiverId());
        
        User currentUser = getCurrentUser();
        
        // Verify receiver exists
        User receiver = userRepository.findById(request.getReceiverId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getReceiverId()));
        
        Message message = new Message();
        message.setSenderId(currentUser.getId());
        message.setReceiverId(request.getReceiverId());
        message.setMessage(request.getMessage());
        message.setTimestamp(LocalDateTime.now());
        message.setIsRead(false);
        
        Message savedMessage = messageRepository.save(message);
        log.info("Message sent successfully: {}", savedMessage.getId());
        
        return modelMapper.map(savedMessage, MessageResponse.class);
    }
    
    /**
     * Get chat messages between two users
     */
    public List<MessageResponse> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        log.debug("Fetching messages between users: {} and {}", senderId, receiverId);
        
        User currentUser = getCurrentUser();
        
        // Ensure current user is one of the participants
        if (!currentUser.getId().equals(senderId) && !currentUser.getId().equals(receiverId)) {
            throw new ResourceNotFoundException("Messages", "users", senderId + " and " + receiverId);
        }
        
        List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);
        
        return messages.stream()
            .map(message -> modelMapper.map(message, MessageResponse.class))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all conversations for a user
     */
    public List<Map<String, Object>> getConversationsForUser(Long userId) {
        log.debug("Fetching conversations for user: {}", userId);
        
        User currentUser = getCurrentUser();
        
        // Ensure current user is requesting their own conversations
        if (!currentUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("Conversations", "userId", userId);
        }
        
        List<Long> partnerIds = messageRepository.findConversationPartners(userId);
        List<Map<String, Object>> conversations = new ArrayList<>();
        
        for (Long partnerId : partnerIds) {
            User partner = userRepository.findById(partnerId)
                .orElse(null);
            
            if (partner != null) {
                // Get last message
                List<Message> messages = messageRepository.findMessagesBetweenUsers(userId, partnerId);
                Message lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
                
                Map<String, Object> conversation = new HashMap<>();
                conversation.put("userId", partner.getId());
                conversation.put("userName", partner.getName());
                conversation.put("avatarUrl", partner.getAvatarUrl());
                conversation.put("lastMessage", lastMessage != null ? lastMessage.getMessage() : "");
                conversation.put("lastMessageTime", lastMessage != null ? lastMessage.getTimestamp() : null);
                conversation.put("isRead", lastMessage != null ? lastMessage.getIsRead() : true);
                
                conversations.add(conversation);
            }
        }
        
        return conversations;
    }
    
    /**
     * Mark messages as read
     */
    @Transactional
    public void markMessagesAsRead(Long senderId, Long receiverId) {
        log.debug("Marking messages as read between users: {} and {}", senderId, receiverId);
        
        List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);
        
        messages.stream()
            .filter(message -> message.getReceiverId().equals(receiverId) && !message.getIsRead())
            .forEach(message -> {
                message.setIsRead(true);
                messageRepository.save(message);
            });
    }
    
    /**
     * Get current user from security context
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
