package com.hubstaffmicroservices.tracktime.AllTrackTimeController;

import com.hubstaffmicroservices.tracktime.Models.Shift;
import com.hubstaffmicroservices.tracktime.Services.shiftservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final shiftservice shiftService;

    @Autowired
    public ShiftController(shiftservice shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }

    @GetMapping("/{shiftId}")
    public ResponseEntity<Shift> getShiftById(@PathVariable Long shiftId) {
        Optional<Shift> shift = shiftService.getShiftById(shiftId);
        return shift.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift createdShift = shiftService.createShift(shift);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }

    @PutMapping("/{shiftId}")
    public ResponseEntity<Shift> updateShift(@PathVariable Long shiftId, @RequestBody Shift updatedShift) {
        Shift updated = shiftService.updateShift(shiftId, updatedShift);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long shiftId) {
        shiftService.deleteShift(shiftId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

