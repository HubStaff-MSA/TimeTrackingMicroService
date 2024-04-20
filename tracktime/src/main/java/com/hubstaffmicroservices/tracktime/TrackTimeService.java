package com.hubstaffmicroservices.tracktime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import javax.sound.midi.Track;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Service
@Data
@RequiredArgsConstructor
public class TrackTimeService{


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
//            tracktimescheduled.transferDataFromCacheToDatabase();
            return trackTimeRepository.save(timeTracked);

        }
    }

    public TrackTime saveTrackTimeDataBase(TrackTime timeTracked) {
        return trackTimeRepository.save(timeTracked);
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
