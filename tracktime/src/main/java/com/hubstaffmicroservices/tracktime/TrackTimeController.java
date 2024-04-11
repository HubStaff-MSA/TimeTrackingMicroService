package com.hubstaffmicroservices.tracktime;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/tracktime")
@RequiredArgsConstructor
public class TrackTimeController {

    private final TrackTimeService trackTimeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackTime saveTrackTime(
    ) {
        return trackTimeService.saveTrackTime();
    }

    @GetMapping("/all")
    public List<TrackTime> getAllTrackTimes() {
        return trackTimeService.getAllTrackTimes();
    }
}
