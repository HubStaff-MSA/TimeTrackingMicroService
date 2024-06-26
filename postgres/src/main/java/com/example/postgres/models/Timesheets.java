package com.example.postgres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name="Timesheets")
public class Timesheets {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "timesheet_sequence"
    )
    @SequenceGenerator(
            name="timesheet_sequence",
            sequenceName = "timesheet_sequence",
            allocationSize=1
    )
    @Column(
            unique = true,
            nullable = true
    )
    private Integer timesheetId;

    private Integer userID;
    @Column(
            updatable = false//,
           // nullable = false
    )
    private LocalDate date;
    @Column(
            updatable = false//,
            //nullable = false
    )
    private LocalDate startTime;
    @Column(
            updatable = false//,
           // nullable = false
    )
    private LocalDate endTime;
    @Transient
    private float duration;

    private Integer projectID;

    private Integer taskID;

    private int status;
    private boolean approved;
    public boolean isApproved() {
        return approved;
    }
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
        // Recalculate duration when endTime is set
        calculateDuration();
    }

    public long getDuration() {
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            return duration.toMinutes(); // Return duration in minutes
        } else {
            return 0; // Or handle null values as appropriate for your application
        }
    }
    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getStatus() {
        return status;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }


    public void setStatus(int status) {
        this.status = status;
    }
    private void calculateDuration() {
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            this.duration = duration.toMinutes(); // Or any other unit you prefer
        } else {
            this.duration = 0; // Or handle null values as appropriate for your application
        }
    }
}
