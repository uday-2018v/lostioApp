package com.lostio.controller;

import com.lostio.dto.request.ClaimRequest;
import com.lostio.dto.request.ClaimStatusRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.ClaimResponse;
import com.lostio.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for claim management endpoints.
 */
@RestController
@RequestMapping("/api/claims")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Claim Management", description = "Claim management APIs")
public class ClaimController {
    
    @Autowired
    private ClaimService claimService;
    
    /**
     * Create a new claim.
     */
    @PostMapping
    @Operation(summary = "Create claim", description = "Create a new claim for a report")
    public ResponseEntity<ApiResponse<ClaimResponse>> createClaim(@Valid @RequestBody ClaimRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        ClaimResponse claim = claimService.createClaim(request, email);
        return new ResponseEntity<>(
                ApiResponse.success("Claim created successfully", claim),
                HttpStatus.CREATED
        );
    }
    
    /**
     * Get all claims with optional filters.
     */
    @GetMapping
    @Operation(summary = "Get all claims", description = "Retrieve all claims with optional filtering and pagination")
    public ResponseEntity<ApiResponse<Page<ClaimResponse>>> getAllClaims(
            @RequestParam(required = false) String reportId,
            @RequestParam(required = false) String claimerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ClaimResponse> claims = claimService.getAllClaims(reportId, claimerId, status, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Claims retrieved successfully", claims));
    }
    
    /**
     * Get claim by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get claim by ID", description = "Retrieve a specific claim by ID")
    public ResponseEntity<ApiResponse<ClaimResponse>> getClaimById(@PathVariable String id) {
        ClaimResponse claim = claimService.getClaimById(id);
        return ResponseEntity.ok(ApiResponse.success("Claim retrieved successfully", claim));
    }
    
    /**
     * Get claims for a specific report.
     */
    @GetMapping("/report/{reportId}")
    @Operation(summary = "Get claims by report", description = "Retrieve all claims for a specific report")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getClaimsByReportId(@PathVariable String reportId) {
        List<ClaimResponse> claims = claimService.getClaimsByReportId(reportId);
        return ResponseEntity.ok(ApiResponse.success("Claims retrieved successfully", claims));
    }
    
    /**
     * Update claim status.
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update claim status", description = "Update the status of a claim (PENDING, APPROVED, REJECTED)")
    public ResponseEntity<ApiResponse<ClaimResponse>> updateClaimStatus(
            @PathVariable String id,
            @Valid @RequestBody ClaimStatusRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        ClaimResponse claim = claimService.updateClaimStatus(id, request, email);
        return ResponseEntity.ok(ApiResponse.success("Claim status updated successfully", claim));
    }
    
    /**
     * Delete claim.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete claim", description = "Delete a claim")
    public ResponseEntity<ApiResponse<String>> deleteClaim(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        claimService.deleteClaim(id, email);
        return ResponseEntity.ok(ApiResponse.success("Claim deleted successfully", "Claim deleted"));
    }
}
