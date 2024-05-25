package com.hubstaffmicroservices.tracktime.AllTrackTimeController;

import com.hubstaffmicroservices.tracktime.Models.CalendarEvent;
import com.hubstaffmicroservices.tracktime.Models.User;
import com.hubstaffmicroservices.tracktime.Services.CalendarService;
import com.hubstaffmicroservices.tracktime.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;

    @Autowired
    public CalendarController(CalendarService calendarService, UserService userService) {
        this.calendarService = calendarService;
        this.userService = userService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<CalendarEvent>> getAllEvents() {
        List<CalendarEvent> events = calendarService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<CalendarEvent> getEventById(@PathVariable Long eventId) {
        Optional<CalendarEvent> event = calendarService.getEventById(eventId);
        return event.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/events")
    public ResponseEntity<CalendarEvent> createEvent(@RequestBody CalendarEvent event) {
        // Assuming user details are sent along with the event data
        Optional<User> userOptional = userService.getUserById((long) event.getUser().getUserId());
        if (userOptional.isPresent()) {
            CalendarEvent createdEvent = calendarService.createEvent(event);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<CalendarEvent> updateEvent(@PathVariable Long eventId, @RequestBody CalendarEvent updatedEvent) {
        Optional<CalendarEvent> existingEvent = calendarService.getEventById(eventId);
        if (existingEvent.isPresent()) {
            CalendarEvent updated = calendarService.updateEvent(eventId, updatedEvent);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        calendarService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
