package com.example.postgres.controller;

import com.example.postgres.dto.Timesheetresponse;
import com.example.postgres.models.Timesheets;
import com.example.postgres.service.Timesheet;
import com.example.postgres.dto.TimesheetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class Timesheetscontroller {
    private final Timesheet timesheetService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTimesheets(@RequestBody TimesheetRequest timesheetRequest) {
        timesheetService.createTimesheet(timesheetRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Timesheetresponse> getAllTimesheets(){
       return timesheetService.getAllTimesheets();
    }

}
