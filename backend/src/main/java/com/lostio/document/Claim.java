package com.lostio.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Claim document representing a claim made on a lost/found item report.
 * Stored in MongoDB 'claims' collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "claims")
public class Claim {
    
    /**
     * Unique identifier for the claim (MongoDB ObjectId)
     */
    @Id
    private String id;
    
    /**
     * ID of the report this claim is for
     */
    @Indexed
    private String reportId;
    
    /**
     * ID of the user making the claim
     */
    @Indexed
    private String claimerId;
    
    /**
     * Name of the user making the claim
     */
    private String claimerName;
    
    /**
     * Description of the claim (proof of ownership, etc.)
     */
    private String claimDescription;
    
    /**
     * Status of the claim (PENDING, APPROVED, REJECTED)
     */
    @Indexed
    private String status;
    
    /**
     * Contact information of the claimer
     */
    private String contact;
    
    /**
     * Timestamp when the claim was created
     */
    @CreatedDate
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the claim was last updated
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    /**
     * Additional notes from the claim reviewer
     */
    private String reviewerNotes;
}
