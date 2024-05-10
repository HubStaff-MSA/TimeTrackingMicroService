package com.hubstaffmicroservices.tracktime;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TrackTimeService {

    private final CacheManager cacheManager;

    private final TrackTimeRepository trackTimeRepository;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Queue<TrackTime> TrackTimeQueue = new ConcurrentLinkedQueue<>();

    private TrackTimeScheduled tracktimescheduled;


    private int max_thread ;


    private ThreadPoolExecutor executor;

    // Initialize the thread pool with a given maximum number of threads
    public void initThreadPool(int maxThreads) {
        executor = new ThreadPoolExecutor(
                0, // Core pool size (0 to allow flexible scaling)
                maxThreads, // Maximum pool size (set based on the provided maxThreads parameter)
                60L, // Keep-alive time for idle threads
                TimeUnit.SECONDS, // Keep-alive time unit
                new LinkedBlockingQueue<>(max_thread +10) // Task queue (LinkedBlockingQueue is appropriate for a variety of situations)
        );
    }

    // Submit a task to the thread pool executor
    public void executeTrackTimeTask(Runnable task) {
        executor.execute(task);
    }

    // Gracefully shut down the executor
    public void shutdownThreadPool() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @CachePut(value = "myCache", key = "3", cacheManager = "cacheManager")
    public TrackTime saveTrackTime() {

        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        LocalDateTime endTimeToUse = endTime != null ? endTime : LocalDateTime.now();

        // Check if there's an existing entry for the same user ID
//        TrackTime existingTrackTime = trackTimeRepository.findByUserId(3);
        Cache cache = cacheManager.getCache("myCache");
        RedisCache.ValueWrapper valueWrapper = cache.get("3");
        TrackTime existingTrackTime;
        if(valueWrapper != null) {
            existingTrackTime = (TrackTime) valueWrapper.get();
        }
        else {
            existingTrackTime = null;
        }
        if (existingTrackTime != null) {
            // Update endTime for the existing entry

            existingTrackTime.setEndTime(endTimeToUse);
//            cache.put("3",existingTrackTime);
//            return trackTimeRepository.save(existingTrackTime);
            return existingTrackTime;
        } else {
            // Create a new entry
            var timeTracked = TrackTime.builder()
                    .userId(3)
                    .project("create HubStaff")
                    .startTime(startTime)
                    .endTime(endTime)
                    .day(LocalDate.now())
                    .build();
//            tracktimescheduled.transferDataFromCacheToDatabase();
//            return trackTimeRepository.save(timeTracked);
            return timeTracked;

        }
    }

    public void saveTrackTimeDataBase(TrackTime timeTracked) {

        trackTimeRepository.save(timeTracked);
    }



    @Cacheable(value = "trackTimes" , key = "#root.methodName" , cacheManager = "cacheManager")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeRepository.findAll();
    }
    public TrackTime executeTrackTimeTask() throws ExecutionException, InterruptedException {
        // Initialize the thread pool with the given maxThreads parameter
        initThreadPool(max_thread);

        Future<TrackTime> future = executor.submit(this::saveTrackTime);
        TrackTime result = future.get(); // This blocks until the task is complete

        // Gracefully shut down the thread pool
        shutdownThreadPool();

        return result;
    }

    public Queue<TrackTime> addCommand() {
        TrackTime tracktime = TrackTime.builder().build();
        TrackTimeQueue.add(tracktime);
        return TrackTimeQueue;
    }

    public TrackTime excute()
    {
        TrackTime trackTime = TrackTimeQueue.poll();
        TrackTime currentTrackTime = saveTrackTime();
        trackTime.setId(currentTrackTime.getId());
        trackTime.setStartTime(currentTrackTime.getStartTime());
        trackTime.setEndTime(currentTrackTime.getEndTime());
        trackTime.setUserId(currentTrackTime.getUserId());
        return trackTime;
    }

    public TrackTime updateTracktime(Map<String,String> newtracktime) {
        TrackTime trackTime = new TrackTime();
        trackTime.updateAttributeNames(newtracktime);
        return trackTime;
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
