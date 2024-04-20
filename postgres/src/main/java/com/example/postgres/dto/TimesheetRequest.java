package com.example.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetRequest {
    private Integer timesheetId;

    private Integer userID;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private float duration;

    private Integer projectID;

    private Integer taskID;

    private int status;
    private boolean approved;
}
