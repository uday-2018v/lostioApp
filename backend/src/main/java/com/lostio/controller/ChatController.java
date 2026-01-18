package com.lostio.controller;

import com.lostio.model.Message;
import com.lostio.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    // Send message
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            message.setSentAt(LocalDateTime.now());
            message.setRead(false);
            Message savedMessage = messageRepository.save(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Message sent successfully");
            response.put("data", savedMessage);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get messages between two users
    @GetMapping
    public ResponseEntity<?> getMessages(
            @RequestParam String senderId, 
            @RequestParam String receiverId) {
        try {
            List<Message> messages = messageRepository
                    .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAt(
                            senderId, receiverId, receiverId, senderId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Messages fetched successfully");
            response.put("data", messages);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
