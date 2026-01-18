package com.lostio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a claim.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    
    @NotBlank(message = "Report ID is required")
    private String reportId;
    
    @NotBlank(message = "Claim description is required")
    private String claimDescription;
    
    private String contact;
}
