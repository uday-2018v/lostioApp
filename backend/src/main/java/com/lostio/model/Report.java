package com.lostio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private String location;
    
    private String photoUrl;
    
    private String userId;
    
    private String userName;
    
    private String status; // "LOST", "FOUND", "CLAIMED"
    
    private LocalDateTime time;
}
