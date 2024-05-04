package com.hubstaffmicroservices.tracktime;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v2/tracktime")
@RequiredArgsConstructor
public class TrackTimeController {

    private final TrackTimeService trackTimeService;
//    private final DatabaseConfig databaseConfig;
    private final FreezeConfig freezeConfig;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackTime saveTrackTime(
            @RequestBody Map<String, Integer> request
    ) throws ExecutionException, InterruptedException {
//        trackTimeService.setMax_thread(request.get("max_thread"));
//        return trackTimeService.executeTrackTimeTask();
        return trackTimeService.saveTrackTime();
    }

    @PostMapping("/excute")
    public TrackTime excute()
    {
        return trackTimeService.excute();
    }


    @PostMapping("/addCommand")
    public Queue<TrackTime> addCommandSaveTrackTime() {
        return trackTimeService.addCommand();
    }
//    @PostMapping("/maxConnections")
//    public String updateMaxConnections(@RequestBody int newMaxConnections) {
//        databaseConfig.updateMaxDbConnectionsCount(newMaxConnections);
//        return "Max connections updated to " + newMaxConnections;
//    }
    @PostMapping("/freeze")
    public String freezeApplication() {
        freezeConfig.setIsFrozen(true);
        return "Application frozen";
    }

    @PostMapping("/continue")
    public String unfreezeApplication() {
        freezeConfig.setIsFrozen(false);
        return "Application unfrozen";
    }

//    @PostMapping("/update")
//    public TrackTime updateTrackTime(
//            @RequestBody Map<String, String> request
//    )
//        }


    @GetMapping("/all")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeService.getAllTrackTimes();
    }
}
