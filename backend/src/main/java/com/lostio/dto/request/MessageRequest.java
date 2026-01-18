package com.lostio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending a message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    
    @NotBlank(message = "Receiver ID is required")
    private String receiverId;
    
    @NotBlank(message = "Message is required")
    private String message;
}
