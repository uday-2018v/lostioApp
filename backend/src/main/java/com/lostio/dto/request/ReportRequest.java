package com.lostio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating or updating a report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String photoUrl;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private LocalDateTime time;
    
    private String category;
    
    private String contact;
}
