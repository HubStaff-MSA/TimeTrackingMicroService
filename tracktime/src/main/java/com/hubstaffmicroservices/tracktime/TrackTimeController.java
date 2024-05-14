package com.hubstaffmicroservices.tracktime;


import com.hubstaffmicroservices.tracktime.Commands.getTimeSheetCommand;
import com.hubstaffmicroservices.tracktime.Controller.BigController;
import com.hubstaffmicroservices.tracktime.Controller.FreezeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v2/tracktime")
@RequiredArgsConstructor
public class TrackTimeController {

    private final TrackTimeService trackTimeService;
//    private final DatabaseConfig databaseConfig;


//    @PostMapping("/save")
//    @ResponseStatus(HttpStatus.CREATED)
//    public TrackTime saveTrackTime(
//            @RequestBody Map<String, Integer> request
//    ) throws ExecutionException, InterruptedException {
////        trackTimeService.setMax_thread(request.get("max_thread"));
////        return trackTimeService.executeTrackTimeTask();
//        return trackTimeService.saveTrackTime();
//    }
//
//    @PostMapping("/excute")
//    public TrackTime excute()
//    {
//        return trackTimeService.excute();
//    }

//    @GetMapping("/all")
//    public List<TrackTime> getAllTrackTimes() {
//        return trackTimeService.getAllTrackTimes();
//    }

//    @PostMapping("/gettimeSheet")
//    public TrackTime getTimeSheet(@RequestBody Map<String, ?> request) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        Field cmdMap = UpdatedClass.class.getField("cmdMap");
//        Class<?> command = (Class<?>) cmdMap.get(request.get("command"));
//        Method method = command.getMethod("build");
//        method.invoke(request.get("payload"));
//        Field returned = command.getField("returned");
//        return (TrackTime) returned.get(command);
//    }

    @PostMapping("/getTimeSheet")
    public String getTimeSheet(@RequestBody Map<String, ?> request) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field cmdMapField = UpdatedClass.class.getDeclaredField("cmdMap");

        // Make the field accessible
        cmdMapField.setAccessible(true);
        // Get the cmdMap value
        Object cmdMapValue = cmdMapField.get(null);
        // Assuming cmdMapValue is a Map<String, Class<?>>
        ConcurrentHashMap<String, Class<?>> cmdMap = (ConcurrentHashMap<String, Class<?>>) cmdMapValue;

        Class<?> commandClass = (Class<?>) cmdMap.get(request.get("command"));

        Object commandInstance = commandClass.newInstance();

//         Get the build method of the command class
        Method buildMethod = commandClass.getDeclaredMethod("build", String.class);

        // Invoke the build method
        buildMethod.invoke(commandInstance, request.get("payload"));

        // Get the execute method of the command class
        Method executeMethod = commandClass.getDeclaredMethod("excute", null);

        // Invoke the execute method
        executeMethod.invoke(commandInstance);

        // Get the returned field of the command class
        Field returnedField = commandClass.getDeclaredField("returned");

        // Make the field accessible
        returnedField.setAccessible(true);

        // Get the value of the returned field from the command instance
        String returnedValue = (String) returnedField.get(commandInstance);

        // Cast the returned value to TrackTime
        return returnedValue;
    }


    @PostMapping("/saveUserID")
    public int saveUserID(@RequestBody Map<String, Integer> request) {
        return trackTimeService.saveUserID(request.get("userId"));
    }


    @PostMapping("/startTrackTimeSheet")
    public ResponseEntity<?> startTrackTimeSheet(@RequestBody Map<String, String> request) {
       try{
           trackTimeService.startTrackTimeSheet(request.get("project"), request.get("to_do"));
           return ResponseEntity.ok("Track time started successfully");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping("/stopTrackTimeSheet")
    public void stopTrackTimeSheet() {
        trackTimeService.endTrackTimeSheet();
    }


    @GetMapping("/CacheTimeSheet")
    public String CacheTimeSheet() {
        String result = trackTimeService.getListofTracktime();
        return result;
    }
}
