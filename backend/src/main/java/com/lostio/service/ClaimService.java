package com.lostio.service;

import com.lostio.dto.request.ClaimRequest;
import com.lostio.dto.response.ClaimResponse;
import com.lostio.entity.Claim;
import com.lostio.entity.Report;
import com.lostio.entity.User;
import com.lostio.exception.BadRequestException;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.exception.UnauthorizedException;
import com.lostio.repository.ClaimRepository;
import com.lostio.repository.ReportRepository;
import com.lostio.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for claim management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimService {
    
    private final ClaimRepository claimRepository;
    private final ReportRepository reportRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
    
    /**
     * Create a new claim
     */
    @Transactional
    public ClaimResponse createClaim(ClaimRequest request) {
        log.debug("Creating new claim for report: {}", request.getReportId());
        
        User currentUser = userUtil.getCurrentUser();
        
        Report report = reportRepository.findById(request.getReportId())
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", request.getReportId()));
        
        // Check if report is already claimed
        if (report.getStatus() == Report.Status.CLAIMED) {
            throw new BadRequestException("This report has already been claimed");
        }
        
        Claim claim = new Claim();
        claim.setReport(report);
        claim.setClaimer(currentUser);
        claim.setClaimerName(request.getClaimerName());
        claim.setClaimDescription(request.getClaimDescription());
        claim.setStatus(Claim.Status.PENDING);
        
        Claim savedClaim = claimRepository.save(claim);
        log.info("Claim created successfully: {}", savedClaim.getId());
        
        return mapToResponse(savedClaim);
    }
    
    /**
     * Get all claims with pagination
     */
    public Page<ClaimResponse> getAllClaims(Pageable pageable) {
        log.debug("Fetching all claims");
        
        User currentUser = userUtil.getCurrentUser();
        
        // Only admins can view all claims
        if (currentUser.getRole() != User.Role.ADMIN) {
            return claimRepository.findByClaimerId(currentUser.getId(), pageable)
                .map(this::mapToResponse);
        }
        
        Page<Claim> claims = claimRepository.findAll(pageable);
        return claims.map(this::mapToResponse);
    }
    
    /**
     * Get claim by ID
     */
    public ClaimResponse getClaimById(Long id) {
        log.debug("Fetching claim with ID: {}", id);
        
        Claim claim = claimRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
        
        return mapToResponse(claim);
    }
    
    /**
     * Get claims for a specific report
     */
    public List<ClaimResponse> getClaimsByReportId(Long reportId) {
        log.debug("Fetching claims for report: {}", reportId);
        
        User currentUser = userUtil.getCurrentUser();
        
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        // Check if current user is the report owner or admin
        if (!report.getUser().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to view claims for this report");
        }
        
        List<Claim> claims = claimRepository.findByReportId(reportId);
        return claims.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Update claim status
     */
    @Transactional
    public ClaimResponse updateClaimStatus(Long id, String status) {
        log.debug("Updating claim status for ID: {} to {}", id, status);
        
        User currentUser = userUtil.getCurrentUser();
        
        Claim claim = claimRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
        
        Report report = claim.getReport();
        
        // Check if current user is the report owner or admin
        if (!report.getUser().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to update this claim");
        }
        
        Claim.Status newStatus;
        try {
            newStatus = Claim.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid claim status: " + status);
        }
        
        claim.setStatus(newStatus);
        
        // If claim is approved, mark report as claimed
        if (newStatus == Claim.Status.APPROVED) {
            report.setStatus(Report.Status.CLAIMED);
            reportRepository.save(report);
        }
        
        Claim updatedClaim = claimRepository.save(claim);
        log.info("Claim status updated successfully: {}", updatedClaim.getId());
        
        return mapToResponse(updatedClaim);
    }
    
    /**
     * Delete claim
     */
    @Transactional
    public void deleteClaim(Long id) {
        log.debug("Deleting claim with ID: {}", id);
        
        User currentUser = userUtil.getCurrentUser();
        
        Claim claim = claimRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
        
        // Check if current user is the claimer or admin
        if (!claim.getClaimer().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to delete this claim");
        }
        
        claimRepository.delete(claim);
        log.info("Claim deleted successfully: {}", id);
    }
    
    /**
     * Map Claim entity to ClaimResponse DTO
     */
    private ClaimResponse mapToResponse(Claim claim) {
        ClaimResponse response = modelMapper.map(claim, ClaimResponse.class);
        response.setReportId(claim.getReport().getId());
        response.setClaimerId(claim.getClaimer().getId());
        response.setStatus(claim.getStatus().name());
        return response;
    }
}
