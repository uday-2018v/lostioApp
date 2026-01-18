package com.lostio.service;

import com.lostio.document.Report;
import com.lostio.document.User;
import com.lostio.dto.request.ReportRequest;
import com.lostio.dto.response.ReportResponse;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.exception.UnauthorizedException;
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

/**
 * Service for report management operations.
 */
@Service
public class ReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    
    @Autowired
    private ReportRepository reportRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Create a new report.
     * @param request Report request
     * @param userEmail Email of the user creating the report
     * @return Created report response
     */
    public ReportResponse createReport(ReportRequest request, String userEmail) {
        logger.info("Creating new report for user: {}", userEmail);
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        Report report = modelMapper.map(request, Report.class);
        report.setUserId(user.getId());
        report.setUserName(user.getName());
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        
        report = reportRepository.save(report);
        logger.info("Report created successfully with ID: {}", report.getId());
        
        return modelMapper.map(report, ReportResponse.class);
    }
    
    /**
     * Get all reports with optional filtering.
     * @param status Optional status filter
     * @param location Optional location filter
     * @param userId Optional user ID filter
     * @param pageable Pagination parameters
     * @return Page of report responses
     */
    public Page<ReportResponse> getAllReports(String status, String location, String userId, Pageable pageable) {
        logger.info("Fetching reports with filters - status: {}, location: {}, userId: {}", status, location, userId);
        
        Page<Report> reports;
        
        if (status != null && userId != null) {
            reports = reportRepository.findByStatusAndUserId(status, userId, pageable);
        } else if (status != null) {
            reports = reportRepository.findByStatus(status, pageable);
        } else if (userId != null) {
            reports = reportRepository.findByUserId(userId, pageable);
        } else if (location != null) {
            reports = reportRepository.findByLocationIgnoreCase(location, pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }
        
        return reports.map(report -> modelMapper.map(report, ReportResponse.class));
    }
    
    /**
     * Get report by ID.
     * @param reportId Report ID
     * @return Report response
     */
    public ReportResponse getReportById(String reportId) {
        logger.info("Fetching report with ID: {}", reportId);
        
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        return modelMapper.map(report, ReportResponse.class);
    }
    
    /**
     * Update a report.
     * @param reportId Report ID
     * @param request Update request
     * @param userEmail Email of the user updating the report
     * @return Updated report response
     */
    public ReportResponse updateReport(String reportId, ReportRequest request, String userEmail) {
        logger.info("Updating report with ID: {}", reportId);
        
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Check if user is the owner or admin
        if (!report.getUserId().equals(user.getId()) && !user.getRoles().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to update this report");
        }
        
        // Update fields
        if (request.getTitle() != null) {
            report.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            report.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            report.setLocation(request.getLocation());
        }
        if (request.getPhotoUrl() != null) {
            report.setPhotoUrl(request.getPhotoUrl());
        }
        if (request.getStatus() != null) {
            report.setStatus(request.getStatus());
        }
        if (request.getTime() != null) {
            report.setTime(request.getTime());
        }
        if (request.getCategory() != null) {
            report.setCategory(request.getCategory());
        }
        if (request.getContact() != null) {
            report.setContact(request.getContact());
        }
        
        report.setUpdatedAt(LocalDateTime.now());
        report = reportRepository.save(report);
        
        logger.info("Report updated successfully: {}", reportId);
        return modelMapper.map(report, ReportResponse.class);
    }
    
    /**
     * Delete a report.
     * @param reportId Report ID
     * @param userEmail Email of the user deleting the report
     */
    public void deleteReport(String reportId, String userEmail) {
        logger.info("Deleting report with ID: {}", reportId);
        
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Check if user is the owner or admin
        if (!report.getUserId().equals(user.getId()) && !user.getRoles().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to delete this report");
        }
        
        reportRepository.delete(report);
        logger.info("Report deleted successfully: {}", reportId);
    }
    
    /**
     * Search reports by keyword.
     * @param keyword Search keyword
     * @param pageable Pagination parameters
     * @return Page of report responses
     */
    public Page<ReportResponse> searchReports(String keyword, Pageable pageable) {
        logger.info("Searching reports with keyword: {}", keyword);
        
        Page<Report> reports = reportRepository.searchReports(keyword, pageable);
        return reports.map(report -> modelMapper.map(report, ReportResponse.class));
    }
    
    /**
     * Upload report photo.
     * @param reportId Report ID
     * @param photoUrl Photo URL
     * @param userEmail Email of the user
     * @return Updated report response
     */
    public ReportResponse uploadPhoto(String reportId, String photoUrl, String userEmail) {
        logger.info("Uploading photo for report: {}", reportId);
        
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Check if user is the owner
        if (!report.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to upload photo for this report");
        }
        
        report.setPhotoUrl(photoUrl);
        report.setUpdatedAt(LocalDateTime.now());
        report = reportRepository.save(report);
        
        logger.info("Photo uploaded successfully for report: {}", reportId);
        return modelMapper.map(report, ReportResponse.class);
    }
}
