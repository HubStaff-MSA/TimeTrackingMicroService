package com.example.postgres.repositories;

import com.example.postgres.models.Timesheets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimesheetRepo extends JpaRepository<Timesheets,Integer> {
    // Query method to find timesheets by user ID
    List<Timesheets> findByUserId(Integer userId);

    // Query method to find timesheets by project ID
    List<Timesheets> findByProjectId(Integer projectId);

    // Query method to find timesheets by date range
    List<Timesheets> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Custom repository method to find approved timesheets
    List<Timesheets> findByApprovedTrue();

    // Custom repository method to find timesheets by user ID and project ID
    List<Timesheets> findByUserIdAndProjectId(Integer userId, Integer projectId);

    // Custom repository method to delete timesheets by user ID
    void deleteByUserId(Integer userId);

    // Custom repository method to delete timesheets by project ID
    void deleteByProjectId(Integer projectId);
}
