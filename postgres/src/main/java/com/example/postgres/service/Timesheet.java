package com.example.postgres.service;

import com.example.postgres.dto.TimesheetRequest;
import com.example.postgres.dto.Timesheetresponse;
import com.example.postgres.models.Timesheets;
import com.example.postgres.repositories.TimesheetRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Timesheet {
    private final TimesheetRepo timesheetRepo;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void closeTimeSheetsDaily() {
        closeTimeSheets();
    }

    private void closeTimeSheets() {
        List<Timesheets> allTimeSheets = timesheetRepo.findAll();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Timesheets timesheet : allTimeSheets) {
            LocalDateTime endTime = timesheet.getEndDateFromPayPeriod();

            // If the current date and time is after the end time from pay period
            if (currentDateTime.isAfter(endTime)) {
                if (!timesheet.getStatus().equals("closed")) { // Avoid redundant updates
                    timesheet.setStatus("closed");
                    timesheetRepo.save(timesheet);
                    log.info("Timesheet with ID {} is closed.", timesheet.getId());
                }
            } else {
                if (timesheet.getStatus().equals("closed")) { // If somehow it was closed, but now it's still within the period
                    timesheet.setStatus("open");
                    timesheetRepo.save(timesheet);
                    log.info("Timesheet with ID {} is reopened.", timesheet.getId());
                }
            }
        }

    }
    public void approveTimesheet(Long timesheetId, String approvedBy) {
        Optional<Timesheets> timesheetsOptional = timesheetRepo.findById(timesheetId);
        if (timesheetsOptional.isPresent()) {
            Timesheets timesheets = timesheetsOptional.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime endTime = timesheets.getEndDateFromPayPeriod();
            if (currentDateTime.isAfter(endTime)) {
                timesheets.setActions("approved");
                timesheets.setApprovedBy(approvedBy);
                timesheetRepo.save(timesheets);
                log.info("Timesheet with ID {} is approved by {}.", timesheetId, approvedBy);
            } else {
                log.error("Cannot approve the timesheet as the pay period has not ended yet.");
            }
        } else {
            log.error("Timesheet with ID {} not found.", timesheetId);
        }
    }
    public void denyTimesheet(Long timesheetId, String denyComments) {
        Optional<Timesheets> timesheetsOptional = timesheetRepo.findById(timesheetId);
        if (timesheetsOptional.isPresent()) {
            Timesheets timesheets = timesheetsOptional.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime endTime = timesheets.getEndDateFromPayPeriod();
            if (currentDateTime.isAfter(endTime)) {
                timesheets.setActions("deny");
                timesheets.setDenyComments(denyComments);
                timesheetRepo.save(timesheets);
                log.info("Timesheet with ID {} is denied with comments: {}.", timesheetId, denyComments);
            } else {
                log.error("Cannot deny the timesheet as the pay period has not ended yet.");
            }
        } else {
            log.error("Timesheet with ID {} not found.", timesheetId);
        }
    }
    public List<Timesheets> getAllTimesheets() {
        return timesheetRepo.findAll();
    }

    public Timesheets getTimesheetById(Long id) {
        Optional<Timesheets> timesheetOptional = timesheetRepo.findById(id);
        if (timesheetOptional.isPresent()) {
            return timesheetOptional.get();
        } else {
            log.error("Timesheet with ID {} not found.", id);
            return null; // Or throw an exception as per your requirement
        }
    }
   @Autowired
   public void createTimesheet(TimesheetRequest timesheetRequest){
        Timesheets timesheet = Timesheets.builder()
                .id(timesheetRequest.getId())
                .memberId(timesheetRequest.getMemberId())
                .payPeriod(timesheetRequest.getPayPeriod())
                .duration(timesheetRequest.getDuration())
                .actions(timesheetRequest.getActions())
                .status(timesheetRequest.getStatus())
                .build();

        // Save the constructed timesheet to the database
        timesheetRepo.save(timesheet);
        log.info("timesheet {} is saved"+ timesheet.getId());
    }

    public void deletetimesheet(Long timesheetId){
        boolean exists=timesheetRepo.existsById(timesheetId);
        if(!exists){
            throw new IllegalStateException("timesheet with id"+timesheetId+"does not exists");
        }
        timesheetRepo.deleteById(timesheetId);
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
