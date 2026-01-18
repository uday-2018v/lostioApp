package com.lostio.controller;

import com.lostio.dto.request.MessageRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.ConversationResponse;
import com.lostio.dto.response.MessageResponse;
import com.lostio.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for chat and messaging endpoints.
 */
@RestController
@RequestMapping("/api/chat")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Chat Management", description = "Chat and messaging APIs")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Send a message.
     */
    @PostMapping
    @Operation(summary = "Send message", description = "Send a chat message to another user")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(@Valid @RequestBody MessageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        MessageResponse message = chatService.sendMessage(request, email);
        
        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSend("/topic/messages/" + request.getReceiverId(), message);
        
        return new ResponseEntity<>(
                ApiResponse.success("Message sent successfully", message),
                HttpStatus.CREATED
        );
    }
    
    /**
     * Get messages between two users.
     */
    @GetMapping
    @Operation(summary = "Get messages", description = "Get chat messages between two users")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getMessages(
            @RequestParam String senderId,
            @RequestParam String receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<MessageResponse> messages = chatService.getMessagesBetweenUsers(senderId, receiverId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Messages retrieved successfully", messages));
    }
    
    /**
     * Get all conversations for a user.
     */
    @GetMapping("/conversations/{userId}")
    @Operation(summary = "Get conversations", description = "Get all conversations for a user")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getConversations(@PathVariable String userId) {
        List<ConversationResponse> conversations = chatService.getConversations(userId);
        return ResponseEntity.ok(ApiResponse.success("Conversations retrieved successfully", conversations));
    }
    
    /**
     * Mark message as read.
     */
    @PutMapping("/{messageId}/read")
    @Operation(summary = "Mark message as read", description = "Mark a message as read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable String messageId) {
        chatService.markAsRead(messageId);
        return ResponseEntity.ok(ApiResponse.success("Message marked as read", "Success"));
    }
    
    /**
     * Get unread message count.
     */
    @GetMapping("/unread/{userId}")
    @Operation(summary = "Get unread count", description = "Get unread message count for a user")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable String userId) {
        long count = chatService.getUnreadMessageCount(userId);
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved", count));
    }
    
    /**
     * WebSocket endpoint for real-time chat.
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageResponse sendMessageViaWebSocket(@Payload MessageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return chatService.sendMessage(request, email);
    }
}
