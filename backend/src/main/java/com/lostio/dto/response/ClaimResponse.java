package com.lostio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for claim response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {
    private String id;
    private String reportId;
    private String claimerId;
    private String claimerName;
    private String claimDescription;
    private String status;
    private String contact;
    private String reviewerNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
