package com.lostio.service;

import com.lostio.dto.request.ReportRequest;
import com.lostio.dto.response.ReportResponse;
import com.lostio.entity.Report;
import com.lostio.entity.User;
import com.lostio.exception.ResourceNotFoundException;
import com.lostio.exception.UnauthorizedException;
import com.lostio.repository.ReportRepository;
import com.lostio.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for report management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
    
    /**
     * Create a new report
     */
    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        log.debug("Creating new report: {}", request.getTitle());
        
        User currentUser = userUtil.getCurrentUser();
        
        Report report = new Report();
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLocation(request.getLocation());
        report.setPhotoUrl(request.getPhotoUrl());
        report.setUser(currentUser);
        report.setUserName(currentUser.getName());
        
        if (request.getStatus() != null) {
            report.setStatus(Report.Status.valueOf(request.getStatus().toUpperCase()));
        }
        
        report.setTime(request.getTime() != null ? request.getTime() : LocalDateTime.now());
        
        Report savedReport = reportRepository.save(report);
        log.info("Report created successfully: {}", savedReport.getId());
        
        return mapToResponse(savedReport);
    }
    
    /**
     * Get all reports with pagination
     */
    public Page<ReportResponse> getAllReports(Pageable pageable) {
        log.debug("Fetching all reports");
        
        Page<Report> reports = reportRepository.findAll(pageable);
        return reports.map(this::mapToResponse);
    }
    
    /**
     * Get report by ID
     */
    public ReportResponse getReportById(Long id) {
        log.debug("Fetching report with ID: {}", id);
        
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
        
        return mapToResponse(report);
    }
    
    /**
     * Update report
     */
    @Transactional
    public ReportResponse updateReport(Long id, ReportRequest request) {
        log.debug("Updating report with ID: {}", id);
        
        User currentUser = userUtil.getCurrentUser();
        
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
        
        // Check if current user is the owner or admin
        if (!report.getUser().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
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
            report.setStatus(Report.Status.valueOf(request.getStatus().toUpperCase()));
        }
        if (request.getTime() != null) {
            report.setTime(request.getTime());
        }
        
        Report updatedReport = reportRepository.save(report);
        log.info("Report updated successfully: {}", updatedReport.getId());
        
        return mapToResponse(updatedReport);
    }
    
    /**
     * Delete report
     */
    @Transactional
    public void deleteReport(Long id) {
        log.debug("Deleting report with ID: {}", id);
        
        User currentUser = userUtil.getCurrentUser();
        
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
        
        // Check if current user is the owner or admin
        if (!report.getUser().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedException("You are not authorized to delete this report");
        }
        
        reportRepository.delete(report);
        log.info("Report deleted successfully: {}", id);
    }
    
    /**
     * Search reports by keyword
     */
    public Page<ReportResponse> searchReports(String keyword, Pageable pageable) {
        log.debug("Searching reports with keyword: {}", keyword);
        
        Page<Report> reports = reportRepository.searchReports(keyword, pageable);
        return reports.map(this::mapToResponse);
    }
    
    /**
     * Map Report entity to ReportResponse DTO
     */
    private ReportResponse mapToResponse(Report report) {
        ReportResponse response = modelMapper.map(report, ReportResponse.class);
        response.setUserId(report.getUser().getId());
        response.setStatus(report.getStatus().name());
        return response;
    }
}
