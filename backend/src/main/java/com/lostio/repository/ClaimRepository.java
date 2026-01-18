package com.lostio.repository;

import com.lostio.model.Claim;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends MongoRepository<Claim, String> {
    
    List<Claim> findByReportId(String reportId);
    
    List<Claim> findByClaimantId(String claimantId);
}
