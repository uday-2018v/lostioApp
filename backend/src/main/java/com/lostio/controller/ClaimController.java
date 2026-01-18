package com.lostio.controller;

import com.lostio.dto.request.ClaimRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.ClaimResponse;
import com.lostio.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for claim management endpoints
 */
@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Claims", description = "Claim management endpoints")
public class ClaimController {
    
    private final ClaimService claimService;
    
    /**
     * Create a new claim
     */
    @PostMapping
    @Operation(summary = "Create claim", description = "Create a new claim for a report")
    public ResponseEntity<ApiResponse<ClaimResponse>> createClaim(
            @Valid @RequestBody ClaimRequest request) {
        ClaimResponse claim = claimService.createClaim(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Claim created successfully", claim));
    }
    
    /**
     * Get all claims with pagination
     */
    @GetMapping
    @Operation(summary = "Get all claims", description = "Get paginated list of claims (admin/owner)")
    public ResponseEntity<ApiResponse<Page<ClaimResponse>>> getAllClaims(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ClaimResponse> claims = claimService.getAllClaims(pageable);
        return ResponseEntity.ok(ApiResponse.success(claims));
    }
    
    /**
     * Get claim by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get claim", description = "Get claim details by ID")
    public ResponseEntity<ApiResponse<ClaimResponse>> getClaimById(@PathVariable Long id) {
        ClaimResponse claim = claimService.getClaimById(id);
        return ResponseEntity.ok(ApiResponse.success(claim));
    }
    
    /**
     * Get claims for a specific report
     */
    @GetMapping("/report/{reportId}")
    @Operation(summary = "Get claims by report", description = "Get all claims for a specific report")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getClaimsByReportId(
            @PathVariable Long reportId) {
        List<ClaimResponse> claims = claimService.getClaimsByReportId(reportId);
        return ResponseEntity.ok(ApiResponse.success(claims));
    }
    
    /**
     * Update claim status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update claim status", description = "Update claim status (PENDING, APPROVED, REJECTED)")
    public ResponseEntity<ApiResponse<ClaimResponse>> updateClaimStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        ClaimResponse claim = claimService.updateClaimStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Claim status updated successfully", claim));
    }
    
    /**
     * Delete claim
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete claim", description = "Delete a claim")
    public ResponseEntity<ApiResponse<String>> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.ok(ApiResponse.success("Claim deleted successfully", null));
    }
}
