package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for report response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private String location;
    
    private String photoUrl;
    
    private Long userId;
    
    private String userName;
    
    private String status;
    
    private LocalDateTime time;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
