package com.lostio.controller;

import com.lostio.dto.request.ReportRequest;
import com.lostio.dto.response.ApiResponse;
import com.lostio.dto.response.FileUploadResponse;
import com.lostio.dto.response.ReportResponse;
import com.lostio.service.FileUploadService;
import com.lostio.service.ReportService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for lost/found item reports
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Reports", description = "Lost and found item report endpoints")
public class ReportController {
    
    private final ReportService reportService;
    private final FileUploadService fileUploadService;
    
    /**
     * Create a new report
     */
    @PostMapping
    @Operation(summary = "Create report", description = "Create a new lost/found item report")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(
            @Valid @RequestBody ReportRequest request) {
        ReportResponse report = reportService.createReport(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Report created successfully", report));
    }
    
    /**
     * Get all reports with pagination
     */
    @GetMapping
    @Operation(summary = "Get all reports", description = "Get paginated list of all reports")
    public ResponseEntity<ApiResponse<Page<ReportResponse>>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") 
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<ReportResponse> reports = reportService.getAllReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }
    
    /**
     * Get report by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get report", description = "Get report details by ID")
    public ResponseEntity<ApiResponse<ReportResponse>> getReportById(@PathVariable Long id) {
        ReportResponse report = reportService.getReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
    
    /**
     * Update report
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update report", description = "Update an existing report")
    public ResponseEntity<ApiResponse<ReportResponse>> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequest request) {
        ReportResponse report = reportService.updateReport(id, request);
        return ResponseEntity.ok(ApiResponse.success("Report updated successfully", report));
    }
    
    /**
     * Delete report
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete report", description = "Delete a report")
    public ResponseEntity<ApiResponse<String>> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok(ApiResponse.success("Report deleted successfully", null));
    }
    
    /**
     * Search reports
     */
    @GetMapping("/search")
    @Operation(summary = "Search reports", description = "Search reports by keyword in title, description, or location")
    public ResponseEntity<ApiResponse<Page<ReportResponse>>> searchReports(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReportResponse> reports = reportService.searchReports(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }
    
    /**
     * Upload report photo
     */
    @PostMapping("/{id}/photo")
    @Operation(summary = "Upload photo", description = "Upload photo for a report")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadReportPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse uploadResponse = fileUploadService.uploadImage(file, "reports");
        
        // Update report photo URL
        ReportRequest updateRequest = new ReportRequest();
        updateRequest.setPhotoUrl(uploadResponse.getUrl());
        reportService.updateReport(id, updateRequest);
        
        return ResponseEntity.ok(
            ApiResponse.success("Photo uploaded successfully", uploadResponse));
    }
}
