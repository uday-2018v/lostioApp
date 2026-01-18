package com.lostio.service;

import com.lostio.model.Report;
import com.lostio.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    // Create new report
    public Report createReport(Report report) {
        report.setTime(LocalDateTime.now());
        return reportRepository.save(report);
    }

    // Get all reports
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Get report by ID
    public Optional<Report> getReportById(String id) {
        return reportRepository.findById(id);
    }

    // Update report
    public Report updateReport(String id, Report updatedReport) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        
        report.setTitle(updatedReport.getTitle());
        report.setDescription(updatedReport.getDescription());
        report.setLocation(updatedReport.getLocation());
        report.setPhotoUrl(updatedReport.getPhotoUrl());
        report.setStatus(updatedReport.getStatus());
        
        return reportRepository.save(report);
    }

    // Delete report
    public void deleteReport(String id) {
        reportRepository.deleteById(id);
    }

    // Search reports
    public List<Report> searchReports(String query) {
        return reportRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                query, query);
    }

    // Get reports by user
    public List<Report> getReportsByUserId(String userId) {
        return reportRepository.findByUserId(userId);
    }
}
