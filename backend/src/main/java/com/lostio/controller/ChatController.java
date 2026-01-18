package com.lostio.controller;

import com.lostio.dto.request.MessageRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.MessageResponse;
import com.lostio.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for chat and messaging endpoints
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Chat", description = "Chat and messaging endpoints")
public class ChatController {
    
    private final ChatService chatService;
    
    /**
     * Send a message
     */
    @PostMapping
    @Operation(summary = "Send message", description = "Send a chat message to another user")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request) {
        MessageResponse message = chatService.sendMessage(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Message sent successfully", message));
    }
    
    /**
     * Get chat messages between two users
     */
    @GetMapping
    @Operation(summary = "Get messages", description = "Get chat messages between two users")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        List<MessageResponse> messages = chatService.getMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }
    
    /**
     * Get all conversations for a user
     */
    @GetMapping("/conversations/{userId}")
    @Operation(summary = "Get conversations", description = "Get all conversations for a user")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getConversations(
            @PathVariable Long userId) {
        List<Map<String, Object>> conversations = chatService.getConversationsForUser(userId);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }
    
    /**
     * WebSocket endpoint for real-time chat
     * Messages sent to /app/chat.send will be broadcast to /topic/messages
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageResponse sendMessageWebSocket(MessageRequest request) {
        return chatService.sendMessage(request);
    }
}
