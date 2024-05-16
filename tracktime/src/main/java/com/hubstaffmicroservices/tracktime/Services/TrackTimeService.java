package com.hubstaffmicroservices.tracktime.Services;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Repos.TrackTimeRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TrackTimeService {

//    @Autowired
    private final CacheManager cacheManager;

    private final TrackTimeRepository trackTimeRepository;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private Queue<TrackTime> TrackTimeQueue = new ConcurrentLinkedQueue<>();

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

//    @CachePut(value = "myCache", key = "3", cacheManager = "cacheManager")
//    public TrackTime saveTrackTime() {
//
//        if (startTime == null) {
//            startTime = LocalDateTime.now();
//        }
//        LocalDateTime endTimeToUse = endTime != null ? endTime : LocalDateTime.now();
//
//        // Check if there's an existing entry for the same user ID
////        TrackTime existingTrackTime = trackTimeRepository.findByUserId(3);
//        Cache cache = cacheManager.getCache("myCache");
//        RedisCache.ValueWrapper valueWrapper = cache.get("3");
//        TrackTime existingTrackTime;
//        if(valueWrapper != null) {
//            existingTrackTime = (TrackTime) valueWrapper.get();
//        }
//        else {
//            existingTrackTime = null;
//        }
//        if (existingTrackTime != null) {
//            // Update endTime for the existing entry
//
//            existingTrackTime.setEndTime(endTimeToUse);
////            cache.put("3",existingTrackTime);
////            return trackTimeRepository.save(existingTrackTime);
//            return existingTrackTime;
//        } else {
//            // Create a new entry
//            var timeTracked = TrackTime.builder()
//                    .userId(3)
//                    .project("create HubStaff")
//                    .startTime(startTime)
//                    .endTime(endTime)
//                    .day(LocalDate.now())
//                    .build();
////            tracktimescheduled.transferDataFromCacheToDatabase();
////            return trackTimeRepository.save(timeTracked);
//            return timeTracked;
//
//        }
//    }

//    @CachePut(value = "myCache", key = "#userId", cacheManager = "cacheManager")
    public List<TrackTime> startTrackTimeSheet(String project, String to_do) {
        int userId = getUserID();

        List<TrackTime> trackTimes = cacheManager.getCache("myCache").get(userId, List.class);

        // If no existing list found, create a new one
        if (trackTimes == null) {
            trackTimes = new ArrayList<>();
        }
        else{
            for(TrackTime trackTime : trackTimes){
                if(trackTime.getEndTime() == null){
                    throw new RuntimeException("Must Stop the previous track time before starting a new one");
                }
            }
        }


        LocalDateTime startTime = LocalDateTime.now();

        // Create a new TrackTime object
        TrackTime trackTime = TrackTime.builder()
                .userId(userId)
                .project(project)
                .to_do(to_do)
                .startTime(startTime)
                .day(LocalDate.now())
                .build();

        // Get existing list of TrackTime objects from cache


        // Add the new TrackTime object to the list
        trackTimes.add(trackTime);

        // Update the cache with the new list of TrackTime objects
        cacheManager.getCache("myCache").put(userId, trackTimes);

        return trackTimes; // Return the updated list
    }

//    @CachePut(value = "myCache", key = "#result.userId", cacheManager = "cacheManager")
    public List<TrackTime> endTrackTimeSheet() {
        LocalDateTime endTime = LocalDateTime.now();

        int userID = getUserID();
        // Get the list of TrackTime objects from the cache
        List<TrackTime> trackTimes = cacheManager.getCache("myCache").get(userID, List.class); // Assuming userId is 3

        // Check if the list exists
        if (trackTimes != null) {
            // Iterate through the list to find the TrackTime object to update
            for (TrackTime trackTime : trackTimes) {
                // Update the end time of the trackTime
                if (trackTime.getEndTime() == null) {
                    trackTime.setEndTime(endTime);
                    break; // Exit loop after updating the first trackTime
                }
            }

            // Update the list in the cache
            cacheManager.getCache("myCache").put(userID, trackTimes); // Assuming userId is 3
        } else {
            // Handle the case where the list of TrackTime objects doesn't exist
            // You might want to throw an exception or return a default value
            throw new RuntimeException("List of TrackTime objects not found in cache");
        }

        return trackTimes; // Return the updated TrackTime object
    }



    @Cacheable(value = "myCache", key = "'userId'", cacheManager = "cacheManager")
    public int saveUserID(int userId) {
        return userId;
    }


    public int getUserID() {
        Integer userId = cacheManager.getCache("myCache").get("userId", Integer.class);

        if (userId == null) {
            throw new RuntimeException("User ID not found in cache");
        }
        return userId;
    }
    public void saveTrackTimeDataBase(TrackTime timeTracked) {

        trackTimeRepository.save(timeTracked);
    }



    public String getListofTracktime() {
        List<TrackTime> trackTimes = cacheManager.getCache("myCache").get(getUserID(), List.class);
        String result = "";
        if (trackTimes != null) {
            for (TrackTime trackTime : trackTimes) {
                result += trackTime.toString() + "\n";
            }
        }
        return result;
    }


    public TrackTime getTimeSheet(int id , int organisationID) {
        return trackTimeRepository.findByIdAndOrganizationID(id,organisationID);
    }


//    public Queue<TrackTime> addCommand() {
//        TrackTime tracktime = TrackTime.builder().build();
//        TrackTimeQueue.add(tracktime);
//        return TrackTimeQueue;
//    }




//    public TrackTime excute()
//    {
//        TrackTime trackTime = TrackTimeQueue.poll();
//        TrackTime currentTrackTime = saveTrackTime();
//        trackTime.setId(currentTrackTime.getId());
//        trackTime.setStartTime(currentTrackTime.getStartTime());
//        trackTime.setEndTime(currentTrackTime.getEndTime());
//        trackTime.setUserId(currentTrackTime.getUserId());
//        return trackTime;
//    }

//    public TrackTime updateTracktime(Map<String,String> newtracktime) {
//        TrackTime trackTime = new TrackTime();
//        trackTime.updateAttributeNames(newtracktime);
//        return trackTime;
//    }




    //    @Cacheable(value = "trackTimes" , key = "#root.methodName" , cacheManager = "cacheManager")
//    public List<TrackTime> getAllTrackTimes() {
//        return trackTimeRepository.findAll();
//    }
//    public TrackTime executeTrackTimeTask() throws ExecutionException, InterruptedException {
//        // Initialize the thread pool with the given maxThreads parameter
//        initThreadPool(max_thread);
//
//        Future<TrackTime> future = executor.submit(this::saveTrackTime);
//        TrackTime result = future.get(); // This blocks until the task is complete
//
//        // Gracefully shut down the thread pool
//        shutdownThreadPool();
//
//        return result;
//    }


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
