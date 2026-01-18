package com.lostio.controller;

import com.lostio.model.Report;
import com.lostio.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Create new report
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        try {
            Report savedReport = reportService.createReport(report);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report created successfully");
            response.put("data", savedReport);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all reports
    @GetMapping
    public ResponseEntity<?> getAllReports() {
        try {
            List<Report> reports = reportService.getAllReports();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reports fetched successfully");
            response.put("data", reports);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get report by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
        try {
            Report report = reportService.getReportById(id)
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report fetched successfully");
            response.put("data", report);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update report
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(@PathVariable String id, @RequestBody Report report) {
        try {
            Report updatedReport = reportService.updateReport(id, report);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report updated successfully");
            response.put("data", updatedReport);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete report
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id) {
        try {
            reportService.deleteReport(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Search reports
    @GetMapping("/search")
    public ResponseEntity<?> searchReports(@RequestParam String query) {
        try {
            List<Report> reports = reportService.searchReports(query);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reports fetched successfully");
            response.put("data", reports);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
