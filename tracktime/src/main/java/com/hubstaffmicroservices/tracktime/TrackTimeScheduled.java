package com.hubstaffmicroservices.tracktime;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;

public class TrackTimeScheduled {
    private CacheManager cacheManager;

    private TrackTimeService trackTimeService; // Assuming you have a service to handle database operations

    @Scheduled(fixedRate = 86400000) // 24 hours in milliseconds
    public void transferDataFromCacheToDatabase() {
        Cache cache = cacheManager.getCache("myCache");
        if (cache != null) {
            // Get data from the cache
            Cache.ValueWrapper valueWrapper = cache.get("key");
            if (valueWrapper != null) {
                TrackTime data = (TrackTime) valueWrapper.get();

                // Transfer data to the database
                trackTimeService.saveTrackTimeDataBase(data);

                // Optionally, clear the cache entry after transferring
                cache.evict("key");
            }
        }
    }
}
