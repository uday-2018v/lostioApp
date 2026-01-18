package com.lostio.repository;

import com.lostio.entity.Report;
import com.lostio.entity.Report.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Report entity operations.
 * Provides database access methods for lost/found item reports.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    /**
     * Find all reports by user ID
     * @param userId the user's ID
     * @param pageable pagination information
     * @return page of reports
     */
    Page<Report> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find all reports by status
     * @param status the report status
     * @param pageable pagination information
     * @return page of reports
     */
    Page<Report> findByStatus(Status status, Pageable pageable);
    
    /**
     * Search reports by title, description, or location
     * @param keyword the search keyword
     * @param pageable pagination information
     * @return page of matching reports
     */
    @Query("SELECT r FROM Report r WHERE " +
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Report> searchReports(@Param("keyword") String keyword, Pageable pageable);
}
