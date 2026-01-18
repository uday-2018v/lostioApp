package com.lostio.repository;

import com.lostio.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    
    List<Report> findByUserId(String userId);
    
    List<Report> findByStatus(String status);
    
    List<Report> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description);
}
