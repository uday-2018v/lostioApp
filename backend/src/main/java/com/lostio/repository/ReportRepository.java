package com.lostio.repository;

import com.lostio.document.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Report document operations.
 * Provides methods to interact with the reports collection in MongoDB.
 */
@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    
    /**
     * Find all reports by status.
     * @param status The status to filter by (LOST, FOUND, CLAIMED)
     * @param pageable Pagination information
     * @return Page of reports with the given status
     */
    Page<Report> findByStatus(String status, Pageable pageable);
    
    /**
     * Find all reports created by a specific user.
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of reports created by the user
     */
    Page<Report> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find all reports by status and user ID.
     * @param status The status to filter by
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of reports matching the criteria
     */
    Page<Report> findByStatusAndUserId(String status, String userId, Pageable pageable);
    
    /**
     * Search reports by title, description, or location containing the search term.
     * @param searchTerm The term to search for
     * @param pageable Pagination information
     * @return Page of reports matching the search criteria
     */
    @Query("{'$or': [" +
           "{'title': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}, " +
           "{'location': {'$regex': ?0, '$options': 'i'}}" +
           "]}")
    Page<Report> searchReports(String searchTerm, Pageable pageable);
    
    /**
     * Find reports by location (case-insensitive).
     * @param location The location to filter by
     * @param pageable Pagination information
     * @return Page of reports in the specified location
     */
    @Query("{'location': {'$regex': ?0, '$options': 'i'}}")
    Page<Report> findByLocationIgnoreCase(String location, Pageable pageable);
}
