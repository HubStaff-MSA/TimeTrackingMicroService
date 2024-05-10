package com.hubstaffmicroservices.tracktime;


import com.hubstaffmicroservices.tracktime.Controller.BigController;
import com.hubstaffmicroservices.tracktime.Controller.FreezeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/all")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeService.getAllTrackTimes();
    }
}
