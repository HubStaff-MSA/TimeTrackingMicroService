package com.hubstaffmicroservices.tracktime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackTimeScheduled {

    private CacheManager cacheManager;

    private TrackTimeService trackTimeService; // Assuming you have a service to handle database operations



    @Scheduled(fixedRate = 000) // 24 hours in milliseconds
    public void transferDataFromCacheToDatabase() {
        Cache cache = cacheManager.getCache("myCache");
        System.out.println("valueWrapper");
        if (cache != null) {
            // Get data from the cache
            Cache.ValueWrapper valueWrapper = cache.get("saveTrackTime");
//            System.out.println(valueWrapper);

            if (valueWrapper != null) {
                TrackTime data = (TrackTime) valueWrapper.get();

                // Transfer data to the database
                trackTimeService.saveTrackTimeDataBase(data);

                // Optionally, clear the cache entry after transferring
                cache.evict("saveTrackTime");
            }
        }
    }
}
