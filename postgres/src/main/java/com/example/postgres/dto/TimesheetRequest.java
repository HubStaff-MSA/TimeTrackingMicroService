package com.example.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetRequest {
  /*  private LInteger timesheetId;

    private Integer userID;
    private LocalDate date;
    private LocalDate startTime;
    private LocalDate endTime;

    private float duration;

    private Integer projectID;

    private Integer taskID;

    private int status;
    private boolean approved;*/
    private Long id;

    private Long memberId;
    private String payPeriod;
    private double duration;
    private String actions;
    private String status;
    private String denyComments;
    private String approvedBy;
    private LocalDateTime createdAt;
}
