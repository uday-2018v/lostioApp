package com.lostio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a claim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    
    @NotNull(message = "Report ID is required")
    private Long reportId;
    
    @NotBlank(message = "Claimer name is required")
    private String claimerName;
    
    private String claimDescription;
}
