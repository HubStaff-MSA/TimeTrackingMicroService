package com.hubstaffmicroservices.tracktime;


import com.hubstaffmicroservices.tracktime.Controller.CommandsMap;
import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.BigService;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v2/tracktime")
@RequiredArgsConstructor
public class TrackTimeController {

    private final TrackTimeService trackTimeService;
//    private final BigService bigService;
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
        Field cmdMapField = CommandsMap.class.getDeclaredField("cmdMap");

        // Make the field accessible
        cmdMapField.setAccessible(true);
        // Get the cmdMap value
        Object cmdMapValue = cmdMapField.get(null);
        // Assuming cmdMapValue is a Map<String, Class<?>>
        ConcurrentHashMap<String, Class<?>> cmdMap = (ConcurrentHashMap<String, Class<?>>) cmdMapValue;

        Class<?> commandClass = (Class<?>) cmdMap.get(request.get("command"));

//        Object commandInstance = commandClass.newInstance();
        Object commandInstance = commandClass.getDeclaredConstructor(TrackTimeService.class).newInstance(trackTimeService);

//         Get the build method of the command class
        Method buildMethod = commandClass.getDeclaredMethod("build", Object.class);

        // Invoke the build method
        buildMethod.invoke(commandInstance, request.get("payload"));

        // Get the execute method of the command class
        Method executeMethod = commandClass.getDeclaredMethod("execute", null);

        // Invoke the execute method
        executeMethod.invoke(commandInstance);

        // Get the returned field of the command class
        Field returnedField = commandClass.getDeclaredField("returned");

        // Make the field accessible
        returnedField.setAccessible(true);

        // Get the value of the returned field from the command instance
        Object returnedValue = (Object) returnedField.get(commandInstance);

        // Cast the returned value to TrackTime
        return returnedValue.toString();
    }


    @PostMapping("/saveUserID")
    public int saveUserID(@RequestBody Map<String, Integer> request) {
        return trackTimeService.saveUserID(request.get("userId"));
    }


    @PostMapping("/startTrackTimeSheet")
    public ResponseEntity<?> startTrackTimeSheet(@RequestBody TrackTime request) {
       try{
           trackTimeService.startTrackTimeSheet(request);
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

    @PostMapping("/addtimesheet")
    public ResponseEntity<?> addTimeSheet(@RequestBody TrackTime request) {
        try {
            trackTimeService.saveTrackTimeDataBase(request);
            return ResponseEntity.ok("Track time saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
