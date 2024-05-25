package com.example.postgres.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name="Approvaltimesheet")
public class Approvaltimesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvaltimesheetId;

    private Long memberId;
    private String payPeriod; // format: "start date - end date"
    private double duration;
    private String actions;
    private String status;
    private String denyComments;
    private String approvedBy;
    private LocalDateTime createdAt;

    public Long getId() {
        return approvaltimesheetId;
    }

    public void setId(Long approvaltimesheetId) {
        this.approvaltimesheetId = approvaltimesheetId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getDenyComments() {
        return denyComments;
    }

    public void setDenyComments(String denyComments) {
        this.denyComments = denyComments;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getStartDateFromPayPeriod() {
        String[] parts = payPeriod.split(" - ");
        String startDateString = parts[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(startDateString, formatter);
    }

    public LocalDateTime getEndDateFromPayPeriod() {
        String[] parts = payPeriod.split(" - ");
        String endDateString = parts[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(endDateString, formatter);
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

