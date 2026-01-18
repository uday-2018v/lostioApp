package com.lostio.controller;

import com.lostio.model.Claim;
import com.lostio.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    @Autowired
    private ClaimRepository claimRepository;

    // Create new claim
    @PostMapping
    public ResponseEntity<?> createClaim(@RequestBody Claim claim) {
        try {
            claim.setCreatedAt(LocalDateTime.now());
            claim.setStatus("PENDING");
            Claim savedClaim = claimRepository.save(claim);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim created successfully");
            response.put("data", savedClaim);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get claims for a report
    @GetMapping("/report/{reportId}")
    public ResponseEntity<?> getClaimsByReportId(@PathVariable String reportId) {
        try {
            List<Claim> claims = claimRepository.findByReportId(reportId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claims fetched successfully");
            response.put("data", claims);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get claims by claimant
    @GetMapping("/user/{claimantId}")
    public ResponseEntity<?> getClaimsByClaimantId(@PathVariable String claimantId) {
        try {
            List<Claim> claims = claimRepository.findByClaimantId(claimantId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claims fetched successfully");
            response.put("data", claims);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
