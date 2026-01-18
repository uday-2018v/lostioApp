package com.lostio.repository;

import com.lostio.document.Claim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Claim document operations.
 * Provides methods to interact with the claims collection in MongoDB.
 */
@Repository
public interface ClaimRepository extends MongoRepository<Claim, String> {
    
    /**
     * Find all claims for a specific report.
     * @param reportId The report ID
     * @return List of claims for the report
     */
    List<Claim> findByReportId(String reportId);
    
    /**
     * Find all claims made by a specific user.
     * @param claimerId The claimer's user ID
     * @param pageable Pagination information
     * @return Page of claims made by the user
     */
    Page<Claim> findByClaimerId(String claimerId, Pageable pageable);
    
    /**
     * Find all claims with a specific status.
     * @param status The claim status (PENDING, APPROVED, REJECTED)
     * @param pageable Pagination information
     * @return Page of claims with the given status
     */
    Page<Claim> findByStatus(String status, Pageable pageable);
    
    /**
     * Find claims by report ID and status.
     * @param reportId The report ID
     * @param status The claim status
     * @return List of claims matching the criteria
     */
    List<Claim> findByReportIdAndStatus(String reportId, String status);
    
    /**
     * Check if a user has already made a claim on a report.
     * @param reportId The report ID
     * @param claimerId The claimer's user ID
     * @return true if claim exists, false otherwise
     */
    boolean existsByReportIdAndClaimerId(String reportId, String claimerId);
}
