package com.lostio.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;

/**
 * Report document representing a lost or found item report.
 * Stored in MongoDB 'reports' collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {
    
    /**
     * Unique identifier for the report (MongoDB ObjectId)
     */
    @Id
    private String id;
    
    /**
     * Title of the lost/found item
     */
    @TextIndexed
    private String title;
    
    /**
     * Detailed description of the item
     */
    @TextIndexed
    private String description;
    
    /**
     * Location where item was lost/found
     */
    @TextIndexed
    @Indexed
    private String location;
    
    /**
     * URL to the photo of the item
     */
    private String photoUrl;
    
    /**
     * ID of the user who created this report
     */
    @Indexed
    private String userId;
    
    /**
     * Name of the user who created this report
     */
    private String userName;
    
    /**
     * Status of the report (LOST, FOUND, CLAIMED)
     */
    @Indexed
    private String status;
    
    /**
     * Time when the item was lost/found
     */
    private LocalDateTime time;
    
    /**
     * Timestamp when the report was created
     */
    @CreatedDate
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the report was last updated
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    /**
     * Category of the item (e.g., Electronics, Documents, etc.)
     */
    private String category;
    
    /**
     * Contact information for the reporter
     */
    private String contact;
}
