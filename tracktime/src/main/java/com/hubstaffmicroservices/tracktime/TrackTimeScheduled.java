package com.hubstaffmicroservices.tracktime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class TrackTimeScheduled {

    private final TrackTimeRepository trackTimeRepository;

    private final CacheManager cacheManager;

    private final TrackTimeService trackTimeService; // Assuming you have a service to handle database operations

    Logger logger = LoggerFactory.getLogger(TrackTimeScheduled.class);

    @Scheduled(fixedRate = 50000) // 24 hours in milliseconds
    public void transferDataFromCacheToDatabase() {

//        System.out.println("Transfer data from cache to database " + LocalDateTime.now());
        Cache cache = cacheManager.getCache("myCache");
        int userId = trackTimeService.getUserID();
//        System.out.println("valueWrapper");
        if (cache != null) {
            // Get data from the cache
            RedisCache.ValueWrapper valueWrapper = cache.get(userId);
//            System.out.println(valueWrapper.toString());

            if (valueWrapper != null) {
                List<TrackTime> data = (List<TrackTime>) valueWrapper.get();
                logger.info("Cached TrackTime: all cached");
//                // Transfer data to the database

                for(TrackTime trackTime : data){
                            Duration newduration = Duration.between(trackTime.getStartTime(), trackTime.getEndTime());
                            Long durationSeconds = newduration.getSeconds();

                            TrackTime newtrackTime = TrackTime.builder()
                            .project(trackTime.getProject())
                            .userId(trackTime.getUserId())
                            .to_do(trackTime.getTo_do())
                            .startTime(trackTime.getStartTime())
                            .day(trackTime.getDay())
                            .endTime(trackTime.getEndTime())
                            .duration(durationSeconds)
                            .build();
                    trackTimeService.saveTrackTimeDataBase(newtrackTime);
                }

//                TrackTime existingTrackTime = trackTimeRepository.findByUserId(3);
//                if (existingTrackTime != null) {
//                    existingTrackTime.setStartTime(data.getStartTime());
//                    existingTrackTime.setEndTime(data.getEndTime());
//                    trackTimeService.saveTrackTimeDataBase(existingTrackTime);
////                    trackTimeRepository.save(existingTrackTime);
//                } else {
//                    trackTimeService.saveTrackTimeDataBase(data);
////                    trackTimeRepository.save(data);
//                }
////
//                // Optionally, clear the cache entry after transferring
                cache.evict(userId);
            }
        }
    }
}
