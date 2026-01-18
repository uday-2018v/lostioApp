package com.lostio.service;

import com.lostio.document.Claim;
import com.lostio.document.Report;
import com.lostio.document.User;
import com.lostio.dto.request.ClaimRequest;
import com.lostio.dto.request.ClaimStatusRequest;
import com.lostio.dto.response.ClaimResponse;
import com.lostio.exception.BadRequestException;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.exception.UnauthorizedException;
import com.lostio.repository.ClaimRepository;
import com.lostio.repository.ReportRepository;
import com.lostio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for claim management operations.
 */
@Service
public class ClaimService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);
    
    @Autowired
    private ClaimRepository claimRepository;
    
    @Autowired
    private ReportRepository reportRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Create a new claim.
     * @param request Claim request
     * @param userEmail Email of the user creating the claim
     * @return Created claim response
     */
    public ClaimResponse createClaim(ClaimRequest request, String userEmail) {
        logger.info("Creating new claim for report: {}", request.getReportId());
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", request.getReportId()));
        
        // Check if user already claimed this report
        if (claimRepository.existsByReportIdAndClaimerId(request.getReportId(), user.getId())) {
            throw new BadRequestException("You have already claimed this report");
        }
        
        // Check if user is trying to claim their own report
        if (report.getUserId().equals(user.getId())) {
            throw new BadRequestException("You cannot claim your own report");
        }
        
        Claim claim = new Claim();
        claim.setReportId(request.getReportId());
        claim.setClaimerId(user.getId());
        claim.setClaimerName(user.getName());
        claim.setClaimDescription(request.getClaimDescription());
        claim.setContact(request.getContact());
        claim.setStatus("PENDING");
        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());
        
        claim = claimRepository.save(claim);
        logger.info("Claim created successfully with ID: {}", claim.getId());
        
        return modelMapper.map(claim, ClaimResponse.class);
    }
    
    /**
     * Get all claims with optional filtering.
     * @param reportId Optional report ID filter
     * @param claimerId Optional claimer ID filter
     * @param status Optional status filter
     * @param pageable Pagination parameters
     * @return Page of claim responses
     */
    public Page<ClaimResponse> getAllClaims(String reportId, String claimerId, String status, Pageable pageable) {
        logger.info("Fetching claims with filters - reportId: {}, claimerId: {}, status: {}", reportId, claimerId, status);
        
        Page<Claim> claims;
        
        if (claimerId != null) {
            claims = claimRepository.findByClaimerId(claimerId, pageable);
        } else if (status != null) {
            claims = claimRepository.findByStatus(status, pageable);
        } else {
            claims = claimRepository.findAll(pageable);
        }
        
        return claims.map(claim -> modelMapper.map(claim, ClaimResponse.class));
    }
    
    /**
     * Get claim by ID.
     * @param claimId Claim ID
     * @return Claim response
     */
    public ClaimResponse getClaimById(String claimId) {
        logger.info("Fetching claim with ID: {}", claimId);
        
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        
        return modelMapper.map(claim, ClaimResponse.class);
    }
    
    /**
     * Get claims for a specific report.
     * @param reportId Report ID
     * @return List of claim responses
     */
    public List<ClaimResponse> getClaimsByReportId(String reportId) {
        logger.info("Fetching claims for report: {}", reportId);
        
        // Verify report exists
        reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        List<Claim> claims = claimRepository.findByReportId(reportId);
        
        return claims.stream()
                .map(claim -> modelMapper.map(claim, ClaimResponse.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Update claim status.
     * @param claimId Claim ID
     * @param request Status update request
     * @param userEmail Email of the user updating the status
     * @return Updated claim response
     */
    public ClaimResponse updateClaimStatus(String claimId, ClaimStatusRequest request, String userEmail) {
        logger.info("Updating claim status for ID: {}", claimId);
        
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        
        final String reportId = claim.getReportId();
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Only report owner or admin can update claim status
        if (!report.getUserId().equals(user.getId()) && !user.getRoles().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to update this claim");
        }
        
        claim.setStatus(request.getStatus());
        if (request.getReviewerNotes() != null) {
            claim.setReviewerNotes(request.getReviewerNotes());
        }
        claim.setUpdatedAt(LocalDateTime.now());
        
        // If claim is approved, update report status to CLAIMED
        if ("APPROVED".equals(request.getStatus())) {
            report.setStatus("CLAIMED");
            report.setUpdatedAt(LocalDateTime.now());
            reportRepository.save(report);
        }
        
        claim = claimRepository.save(claim);
        logger.info("Claim status updated successfully: {}", claimId);
        
        return modelMapper.map(claim, ClaimResponse.class);
    }
    
    /**
     * Delete a claim.
     * @param claimId Claim ID
     * @param userEmail Email of the user deleting the claim
     */
    public void deleteClaim(String claimId, String userEmail) {
        logger.info("Deleting claim with ID: {}", claimId);
        
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Only claim owner or admin can delete
        if (!claim.getClaimerId().equals(user.getId()) && !user.getRoles().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to delete this claim");
        }
        
        claimRepository.delete(claim);
        logger.info("Claim deleted successfully: {}", claimId);
    }
}
