package com.example.postgres.controller;

import com.example.postgres.models.Approvaltimesheet;
import com.example.postgres.service.approvaltimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class Timesheetscontroller {
    private final approvaltimesheetService approvaltimesheetServiceService;

   /* @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTimesheet(@RequestBody TimesheetRequest request) {
        timesheetService.addTimesheet(request);
    }*/

    @GetMapping("/allTimesheets")
    public List<Approvaltimesheet> getAllTimesheets() {
        return approvaltimesheetServiceService.getAllTimesheets();
    }

    @GetMapping("/{id}")
    public Approvaltimesheet getTimesheetById(@PathVariable Long id) {
        return approvaltimesheetServiceService.getTimesheetById(id);
    }

   /* @PutMapping("/{id}")
    public void updateTimesheet(@PathVariable Long id, @RequestBody TimesheetRequest request) {
        timesheetService.updateTimesheet(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
    }*/

    @PutMapping("/{id}/approve")
    public void approveTimesheet(@PathVariable Long id, @RequestParam String approvedBy) {
        approvaltimesheetServiceService.approveTimesheet(id, approvedBy);
    }

    @PutMapping("/{id}/deny")
    public void denyTimesheet(@PathVariable Long id, @RequestParam String denyComments) {
        approvaltimesheetServiceService.denyTimesheet(id, denyComments);
    }

   /* @PostMapping("/createTimesheets")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTimesheets(@RequestBody TimesheetRequest timesheetRequest) {
        timesheetService.createTimesheet(timesheetRequest);
    }
    @GetMapping("/getAllTimesheets")
    @ResponseStatus(HttpStatus.OK)
    public List<Timesheetresponse> getAllTimesheets(){
       return timesheetService.getAllTimesheets();
    }
    @DeleteMapping(path="{timesheetId}")
    public void deletetimesheet(@PathVariable("timesheetId") Integer timesheetId){
        timesheetService.deletetimesheet(timesheetId);
    }
    @PutMapping(path="{timesheetId}")
    public void updateapproval(@PathVariable("timesheetId") Integer timesheetId,
                               @RequestParam(required = false) boolean approved){
         timesheetService.updateapproval(timesheetId);
    }*/

}
