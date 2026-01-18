package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for claim response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {
    
    private Long id;
    
    private Long reportId;
    
    private Long claimerId;
    
    private String claimerName;
    
    private String claimDescription;
    
    private String status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
