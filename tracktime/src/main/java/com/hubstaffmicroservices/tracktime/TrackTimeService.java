package com.hubstaffmicroservices.tracktime;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackTimeService{


    private final TrackTimeRepository trackTimeRepository;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private TrackTimeScheduled tracktimescheduled;

    @Cacheable(value = "myCache", key = "#root.methodName", cacheManager = "cacheManager")
    public TrackTime saveTrackTime() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        LocalDateTime endTimeToUse = endTime != null ? endTime : LocalDateTime.now();

        // Check if there's an existing entry for the same user ID
        TrackTime existingTrackTime = trackTimeRepository.findByUserId(2);

        if (existingTrackTime != null) {
            // Update endTime for the existing entry
            existingTrackTime.setEndTime(endTimeToUse);
            return trackTimeRepository.save(existingTrackTime);
        } else {
            // Create a new entry
            var timeTracked = TrackTime.builder()
                    .userId(2)
                    .startTime(startTime)
                    .endTime(endTimeToUse)
                    .build();
            tracktimescheduled.transferDataFromCacheToDatabase();
            return timeTracked;
        }
    }

    public TrackTime saveTrackTimeDataBase(TrackTime timeTracked) {
        return trackTimeRepository.save(timeTracked);
    }


    @Cacheable(value = "trackTimes" , key = "#root.methodName" , cacheManager = "cacheManager")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeRepository.findAll();
    }




    //    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        // Update endTime when application shuts down
//        endTime = LocalDateTime.now();
//    }
//    public void onApplicationEvent(ContextClosedEvent event) {
//        // Update end time when application shuts down
//        if (startTime != null) {
//            LocalDateTime endTime = LocalDateTime.now();
//            TrackTime lastTrackedTime = trackTimeRepository.findFirstByOrderByStartTimeDesc();
//            if (lastTrackedTime != null) {
//                lastTrackedTime.setEndTime(endTime);
//                trackTimeRepository.save(lastTrackedTime);
//            }
//        }
//    }
}
