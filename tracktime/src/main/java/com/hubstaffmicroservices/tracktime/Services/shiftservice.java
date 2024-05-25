package com.hubstaffmicroservices.tracktime.Services;
import com.hubstaffmicroservices.tracktime.Models.Shift;
import com.hubstaffmicroservices.tracktime.Repos.ShiftRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class shiftservice {

    private final ShiftRepository shiftRepository;

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long shiftId) {

        return shiftRepository.findById(shiftId);
    }

    public Shift createShift(Shift shift) {
        // Additional logic/validation can be added here
        return shiftRepository.save(shift);
    }

    public Shift updateShift(Long shiftId, Shift updatedShift) {
        Optional<Shift> existingShiftOptional = shiftRepository.findById(shiftId);
        if (existingShiftOptional.isPresent()) {
            Shift existingShift = existingShiftOptional.get();
            // Update the existing shift with new data
            existingShift.setShiftName(updatedShift.getShiftName());
            existingShift.setStartTime(updatedShift.getStartTime());
            existingShift.setEndTime(updatedShift.getEndTime());
            // Save the updated shift
            return shiftRepository.save(existingShift);
        } else {
            throw new IllegalArgumentException("Shift with ID " + shiftId + " not found");
        }
    }

    public void deleteShift(Long shiftId) {
        shiftRepository.deleteById(shiftId);
    }
}

