package com.lostio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating claim status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimStatusRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String reviewerNotes;
}
