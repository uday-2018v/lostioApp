package com.lostio.service;

import com.lostio.document.Message;
import com.lostio.document.User;
import com.lostio.dto.request.MessageRequest;
import com.lostio.dto.response.ConversationResponse;
import com.lostio.dto.response.MessageResponse;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.repository.MessageRepository;
import com.lostio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for chat and messaging operations.
 */
@Service
public class ChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Send a message.
     * @param request Message request
     * @param senderEmail Email of the sender
     * @return Message response
     */
    public MessageResponse sendMessage(MessageRequest request, String senderEmail) {
        logger.info("Sending message from: {} to: {}", senderEmail, request.getReceiverId());
        
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", senderEmail));
        
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getReceiverId()));
        
        Message message = new Message();
        message.setSenderId(sender.getId());
        message.setSenderName(sender.getName());
        message.setReceiverId(receiver.getId());
        message.setReceiverName(receiver.getName());
        message.setMessage(request.getMessage());
        message.setTimestamp(LocalDateTime.now());
        message.setIsRead(false);
        
        message = messageRepository.save(message);
        logger.info("Message sent successfully with ID: {}", message.getId());
        
        return modelMapper.map(message, MessageResponse.class);
    }
    
    /**
     * Get messages between two users.
     * @param senderId First user ID
     * @param receiverId Second user ID
     * @param pageable Pagination parameters
     * @return Page of message responses
     */
    public Page<MessageResponse> getMessagesBetweenUsers(String senderId, String receiverId, Pageable pageable) {
        logger.info("Fetching messages between users: {} and {}", senderId, receiverId);
        
        // Create pageable with descending timestamp order
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );
        
        Page<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId, sortedPageable);
        
        // Mark messages as read if current user is the receiver
        messages.getContent().forEach(message -> {
            if (message.getReceiverId().equals(senderId) && !message.getIsRead()) {
                message.setIsRead(true);
                messageRepository.save(message);
            }
        });
        
        return messages.map(message -> modelMapper.map(message, MessageResponse.class));
    }
    
    /**
     * Get all conversations for a user.
     * @param userId User ID
     * @return List of conversation responses
     */
    public List<ConversationResponse> getConversations(String userId) {
        logger.info("Fetching conversations for user: {}", userId);
        
        List<Message> allMessages = messageRepository.findConversationsForUser(userId);
        
        // Group messages by conversation partner
        Map<String, List<Message>> conversationMap = new HashMap<>();
        
        for (Message message : allMessages) {
            String partnerId = message.getSenderId().equals(userId) 
                    ? message.getReceiverId() 
                    : message.getSenderId();
            
            conversationMap.computeIfAbsent(partnerId, k -> new ArrayList<>()).add(message);
        }
        
        // Create conversation responses
        List<ConversationResponse> conversations = new ArrayList<>();
        
        for (Map.Entry<String, List<Message>> entry : conversationMap.entrySet()) {
            String partnerId = entry.getKey();
            List<Message> messages = entry.getValue();
            
            // Get latest message
            Message latestMessage = messages.stream()
                    .max(Comparator.comparing(Message::getTimestamp))
                    .orElse(null);
            
            if (latestMessage != null) {
                // Get partner user info
                User partner = userRepository.findById(partnerId).orElse(null);
                
                if (partner != null) {
                    // Count unread messages from this partner
                    long unreadCount = messages.stream()
                            .filter(m -> m.getReceiverId().equals(userId) && !m.getIsRead())
                            .count();
                    
                    ConversationResponse conversation = new ConversationResponse();
                    conversation.setUserId(partner.getId());
                    conversation.setUserName(partner.getName());
                    conversation.setUserAvatar(partner.getAvatarUrl());
                    conversation.setLastMessage(latestMessage.getMessage());
                    conversation.setLastMessageTime(latestMessage.getTimestamp());
                    conversation.setUnreadCount(unreadCount);
                    
                    conversations.add(conversation);
                }
            }
        }
        
        // Sort by last message time (most recent first)
        conversations.sort(Comparator.comparing(ConversationResponse::getLastMessageTime).reversed());
        
        return conversations;
    }
    
    /**
     * Mark message as read.
     * @param messageId Message ID
     */
    public void markAsRead(String messageId) {
        logger.info("Marking message as read: {}", messageId);
        
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));
        
        message.setIsRead(true);
        messageRepository.save(message);
    }
    
    /**
     * Get unread message count for a user.
     * @param userId User ID
     * @return Number of unread messages
     */
    public long getUnreadMessageCount(String userId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }
}
