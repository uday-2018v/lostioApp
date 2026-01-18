package com.lostio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    
    @Id
    private String id;
    
    private String reportId;
    
    private String claimantId;
    
    private String claimantName;
    
    private String description;
    
    private String status; // "PENDING", "APPROVED", "REJECTED"
    
    private LocalDateTime createdAt;
}
