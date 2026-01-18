package com.lostio.repository;

import com.lostio.entity.Claim;
import com.lostio.entity.Claim.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Claim entity operations.
 * Provides database access methods for claim management.
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    
    /**
     * Find all claims for a specific report
     * @param reportId the report ID
     * @return list of claims
     */
    List<Claim> findByReportId(Long reportId);
    
    /**
     * Find all claims by a specific claimer
     * @param claimerId the claimer's user ID
     * @param pageable pagination information
     * @return page of claims
     */
    Page<Claim> findByClaimerId(Long claimerId, Pageable pageable);
    
    /**
     * Find all claims with a specific status
     * @param status the claim status
     * @param pageable pagination information
     * @return page of claims
     */
    Page<Claim> findByStatus(Status status, Pageable pageable);
}
