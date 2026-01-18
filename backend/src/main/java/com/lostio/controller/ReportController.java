package com.lostio.controller;

import com.lostio.dto.request.ReportRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.ReportResponse;
import com.lostio.service.FileUploadService;
import com.lostio.service.ReportService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for report management endpoints.
 */
@RestController
@RequestMapping("/api/reports")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Report Management", description = "Lost and found report management APIs")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    /**
     * Create a new report.
     */
    @PostMapping
    @Operation(summary = "Create report", description = "Create a new lost or found item report")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(@Valid @RequestBody ReportRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        ReportResponse report = reportService.createReport(request, email);
        return new ResponseEntity<>(
                ApiResponse.success("Report created successfully", report),
                HttpStatus.CREATED
        );
    }
    
    /**
     * Get all reports with optional filters.
     */
    @GetMapping
    @Operation(summary = "Get all reports", description = "Retrieve all reports with optional filtering and pagination")
    public ResponseEntity<ApiResponse<Page<ReportResponse>>> getAllReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ReportResponse> reports = reportService.getAllReports(status, location, userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Reports retrieved successfully", reports));
    }
    
    /**
     * Get report by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID", description = "Retrieve a specific report by ID")
    public ResponseEntity<ApiResponse<ReportResponse>> getReportById(@PathVariable String id) {
        ReportResponse report = reportService.getReportById(id);
        return ResponseEntity.ok(ApiResponse.success("Report retrieved successfully", report));
    }
    
    /**
     * Update report.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update report", description = "Update an existing report")
    public ResponseEntity<ApiResponse<ReportResponse>> updateReport(
            @PathVariable String id,
            @Valid @RequestBody ReportRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        ReportResponse report = reportService.updateReport(id, request, email);
        return ResponseEntity.ok(ApiResponse.success("Report updated successfully", report));
    }
    
    /**
     * Delete report.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete report", description = "Delete a report")
    public ResponseEntity<ApiResponse<String>> deleteReport(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        reportService.deleteReport(id, email);
        return ResponseEntity.ok(ApiResponse.success("Report deleted successfully", "Report deleted"));
    }
    
    /**
     * Search reports.
     */
    @GetMapping("/search")
    @Operation(summary = "Search reports", description = "Search reports by keyword")
    public ResponseEntity<ApiResponse<Page<ReportResponse>>> searchReports(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReportResponse> reports = reportService.searchReports(keyword, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", reports));
    }
    
    /**
     * Upload report photo.
     */
    @PostMapping("/{id}/photo")
    @Operation(summary = "Upload report photo", description = "Upload a photo for the report")
    public ResponseEntity<ApiResponse<ReportResponse>> uploadPhoto(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        String photoUrl = fileUploadService.uploadReportPhoto(file, id);
        ReportResponse report = reportService.uploadPhoto(id, photoUrl, email);
        
        return ResponseEntity.ok(ApiResponse.success("Photo uploaded successfully", report));
    }
}
