package com.example.postgres.repositories;

import com.example.postgres.models.Approvaltimesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepo extends JpaRepository<Approvaltimesheet,Long> {

    List<Approvaltimesheet> findAllByPayPeriodContainingAndId(String payPeriod, Long approvaltimesheetId);
/*
   List<Approvaltimesheet> findByMemberIdAndPayPeriodContaining(Long memberId, String payPeriod);
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
    void deleteByProjectId(Integer projectId);*/
}
