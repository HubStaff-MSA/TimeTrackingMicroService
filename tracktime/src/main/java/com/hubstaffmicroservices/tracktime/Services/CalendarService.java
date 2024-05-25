package com.hubstaffmicroservices.tracktime.Services;

import com.hubstaffmicroservices.tracktime.Models.CalendarEvent;
import com.hubstaffmicroservices.tracktime.Repos.CalendarEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private  final CalendarEventRepository calendarEventRepository;

    public List<CalendarEvent> getAllEvents() {
        return calendarEventRepository.findAll();
    }

    public Optional<CalendarEvent> getEventById(Long eventId) {
        return calendarEventRepository.findById(eventId);
    }

    public CalendarEvent createEvent(CalendarEvent event) {
        // Additional logic/validation can be added here
        return calendarEventRepository.save(event);
    }

    public CalendarEvent updateEvent(Long eventId, CalendarEvent updatedEvent) {
        Optional<CalendarEvent> existingEventOptional = calendarEventRepository.findById(eventId);
        if (existingEventOptional.isPresent()) {
            CalendarEvent existingEvent = existingEventOptional.get();
            // Update the existing event with new data
            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setEventDescription(updatedEvent.getEventDescription());
            existingEvent.setStartDate(updatedEvent.getStartDate());
            existingEvent.setEndDate(updatedEvent.getEndDate());
            // Save the updated event
            return calendarEventRepository.save(existingEvent);
        } else {
            throw new IllegalArgumentException("Event with ID " + eventId + " not found");
        }
    }

    public void deleteEvent(Long eventId) {
        calendarEventRepository.deleteById(eventId);
    }
}
