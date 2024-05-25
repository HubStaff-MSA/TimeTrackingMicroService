package com.hubstaffmicroservices.tracktime.Services;

import com.hubstaffmicroservices.tracktime.Models.Schedule;
import com.hubstaffmicroservices.tracktime.Repos.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public Schedule createSchedule(Schedule schedule) {
        // Additional logic/validation can be added here
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule) {
        Optional<Schedule> existingScheduleOptional = scheduleRepository.findById(scheduleId);
        if (existingScheduleOptional.isPresent()) {
            Schedule existingSchedule = existingScheduleOptional.get();
            // Update the existing schedule with new data
            existingSchedule.setShift(updatedSchedule.getShift());
            existingSchedule.setScheduledDate(updatedSchedule.getScheduledDate());
            // Save the updated schedule
            return scheduleRepository.save(existingSchedule);
        } else {
            throw new IllegalArgumentException("Schedule with ID " + scheduleId + " not found");
        }
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}
