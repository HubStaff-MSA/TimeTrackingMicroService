package com.example.postgres.service;

import com.example.postgres.dto.TimesheetRequest;
import com.example.postgres.models.Approvaltimesheet;
import com.example.postgres.repositories.ApprovalRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class approvaltimesheetService {
    private final ApprovalRepo approvalRepo;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void closeTimeSheetsDaily() {
        closeTimeSheets();
    }

    private void closeTimeSheets() {
        List<Approvaltimesheet> allTimeSheets = approvalRepo.findAll();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Approvaltimesheet timesheet : allTimeSheets) {
            LocalDateTime endTime = timesheet.getEndDateFromPayPeriod();

            // If the current date and time is after the end time from pay period
            if (currentDateTime.isAfter(endTime)) {
                if (!timesheet.getStatus().equals("closed")) { // Avoid redundant updates
                    timesheet.setStatus("closed");
                    approvalRepo.save(timesheet);
                    log.info("Timesheet with ID {} is closed.", timesheet.getId());
                }
            } else {
                if (timesheet.getStatus().equals("closed")) { // If somehow it was closed, but now it's still within the period
                    timesheet.setStatus("open");
                    approvalRepo.save(timesheet);
                    log.info("Timesheet with ID {} is reopened.", timesheet.getId());
                }
            }
        }

    }
    public void approveTimesheet(Long timesheetId, String approvedBy) {
        Optional<Approvaltimesheet> timesheetsOptional = approvalRepo.findById(timesheetId);
        if (timesheetsOptional.isPresent()) {
            Approvaltimesheet approvaltimesheet = timesheetsOptional.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime endTime = approvaltimesheet.getEndDateFromPayPeriod();
            if (currentDateTime.isAfter(endTime)) {
                approvaltimesheet.setActions("approved");
                approvaltimesheet.setApprovedBy(approvedBy);
                approvalRepo.save(approvaltimesheet);
                log.info("Timesheet with ID {} is approved by {}.", timesheetId, approvedBy);
            } else {
                log.error("Cannot approve the timesheet as the pay period has not ended yet.");
            }
        } else {
            log.error("Timesheet with ID {} not found.", timesheetId);
        }
    }
    public void denyTimesheet(Long timesheetId, String denyComments) {
        Optional<Approvaltimesheet> timesheetsOptional = approvalRepo.findById(timesheetId);
        if (timesheetsOptional.isPresent()) {
            Approvaltimesheet approvaltimesheet = timesheetsOptional.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime endTime = approvaltimesheet.getEndDateFromPayPeriod();
            if (currentDateTime.isAfter(endTime)) {
                approvaltimesheet.setActions("deny");
                approvaltimesheet.setDenyComments(denyComments);
                approvalRepo.save(approvaltimesheet);
                log.info("Timesheet with ID {} is denied with comments: {}.", timesheetId, denyComments);
            } else {
                log.error("Cannot deny the timesheet as the pay period has not ended yet.");
            }
        } else {
            log.error("Timesheet with ID {} not found.", timesheetId);
        }
    }
    public List<Approvaltimesheet> getAllTimesheets() {
        return approvalRepo.findAll();
    }

    public Approvaltimesheet getTimesheetById(Long id) {
        Optional<Approvaltimesheet> timesheetOptional = approvalRepo.findById(id);
        if (timesheetOptional.isPresent()) {
            return timesheetOptional.get();
        } else {
            log.error("Timesheet with ID {} not found.", id);
            return null; // Or throw an exception as per your requirement
        }
    }
   @Autowired
   public void createTimesheet(TimesheetRequest timesheetRequest){
        Approvaltimesheet timesheet = Approvaltimesheet.builder()
                .approvaltimesheetId(timesheetRequest.getId())
                .memberId(timesheetRequest.getMemberId())
                .payPeriod(timesheetRequest.getPayPeriod())
                .duration(timesheetRequest.getDuration())
                .actions(timesheetRequest.getActions())
                .status(timesheetRequest.getStatus())
                .build();

        // Save the constructed timesheet to the database
        approvalRepo.save(timesheet);
        log.info("timesheet {} is saved"+ timesheet.getId());
    }

    public void deletetimesheet(Long timesheetId){
        boolean exists= approvalRepo.existsById(timesheetId);
        if(!exists){
            throw new IllegalStateException("timesheet with id"+timesheetId+"does not exists");
        }
        approvalRepo.deleteById(timesheetId);
    }
/*
    // You may need to map between DTO and Entity objects
    private Timesheets mapToEntity(TimesheetRequest dto) {

        Timesheets entity = new Timesheets();
        entity.setTimesheetId(dto.getTimesheetId());
        entity.setUserID(dto.getUserID());
        entity.setDate(dto.getDate());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setDuration(dto.getDuration());
        entity.setProjectID(dto.getProjectID());
        entity.setTaskID(dto.getTaskID());
        entity.setStatus(dto.getStatus());
        entity.setApproved(dto.isApproved());
        return entity;
    }
    @Transactional
    public void updateapproval(Integer timesheetId) {
        Timesheets timesheet = timesheetRepo.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        // Calculate duration in hours
        Duration duration = Duration.between(timesheet.getStartTime().atStartOfDay(), timesheet.getEndTime().atStartOfDay());
        long durationHours = duration.toHours();

        // Update the approved attribute if duration is greater than 8 hours
        if (timesheet.getEndTime().isBefore(LocalDate.now())) {
            if (durationHours > 8) {
                timesheet.setApproved(true);
            } else {
                timesheet.setApproved(false);
            }
        }
        timesheetRepo.save(timesheet);
    }*/
}
