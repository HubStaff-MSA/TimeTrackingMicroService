package com.hubstaffmicroservices.tracktime;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v2/tracktime")
@RequiredArgsConstructor
public class TrackTimeController {

    private final TrackTimeService trackTimeService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackTime saveTrackTime(
            @RequestBody Map<String, Integer> request
    ) throws ExecutionException, InterruptedException {
        trackTimeService.setMax_thread(request.get("max_thread"));
        return trackTimeService.executeTrackTimeTask();
    }

    @GetMapping("/all")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeService.getAllTrackTimes();
    }
}