package com.example.postgres.service;

import com.example.postgres.dto.TimesheetRequest;
import com.example.postgres.dto.Timesheetresponse;
import com.example.postgres.models.Timesheets;
import com.example.postgres.repositories.TimesheetRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Timesheet {
    private final TimesheetRepo timesheetRepo;
    @Autowired
   public void createTimesheet(TimesheetRequest timesheetRequest){
        Timesheets timesheet = Timesheets.builder()
                .userID(timesheetRequest.getUserID())
                .date(timesheetRequest.getDate())
                .startTime(timesheetRequest.getStartTime())
                .endTime(timesheetRequest.getEndTime())
                .duration(timesheetRequest.getDuration())
                .projectID(timesheetRequest.getProjectID())
                .taskID(timesheetRequest.getTaskID())
                .status(timesheetRequest.getStatus())
                .approved(timesheetRequest.isApproved())
                .build();

        // Save the constructed timesheet to the database
        timesheetRepo.save(timesheet);
        log.info("timesheet {} is saved"+ timesheet.getTimesheetId());
    }

    public void approveTimesheet(TimesheetRequest timesheet) {
        Timesheets timesheets=timesheetRepo.findById(timesheet.getTimesheetId())
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));

        // Check if the timesheet period has ended or member is not actively tracking time
        if (!isTimesheetPeriodActive(timesheets)) {
            // Lock the timesheet for editing
            timesheets.setApproved(true);
            // Update the timesheet status as approved
            timesheets.setStatus(1); // You may need to define status constants
            // Save/update the timesheet in your database
            // Example: timesheetRepository.save(timesheet);
            timesheetRepo.save(timesheets);
        } else {
            // Handle the case where the timesheet period is still active
            // You can throw an exception or handle it according to your business logic
            throw new IllegalStateException("Cannot approve timesheet. Timesheet period is still active.");
        }
    }

    private boolean isTimesheetPeriodActive(Timesheets timesheet) {
        LocalDateTime currentTime = LocalDateTime.now();
        // Check if the end time of the timesheet is before the current time
        return timesheet.getEndTime().isBefore(currentTime);
    }
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

    public List<Timesheetresponse> getAllTimesheets() {
       List<Timesheets> timesheet= timesheetRepo.findAll();
       return timesheet.stream().map(this::mapToTimesheetresponse).toList();
    }
    private Timesheetresponse mapToTimesheetresponse(Timesheets timesheett){
        return Timesheetresponse.builder().timesheetId(timesheett.getTimesheetId()).projectID(timesheett.getProjectID())
                .duration(timesheett.getDuration()).approved(timesheett.isApproved()).build();
    }
}
