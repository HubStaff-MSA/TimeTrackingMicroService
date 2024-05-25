package com.example.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timesheetresponse {
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
