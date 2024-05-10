package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.UpdatedClass;

import com.hubstaffmicroservices.tracktime.TrackTime;
import com.hubstaffmicroservices.tracktime.TrackTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/v2/control/tracktime")
@RequiredArgsConstructor
public class ControlApplication {

    private final TrackTimeService trackTimeService;

    private final FreezeConfig freezeConfig;

    private final BigController bigController;
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

    @PostMapping("/update")
    public String updateTrackTime() throws IOException {
        return bigController.replace("com.hubstaffmicroservices.tracktime.UpdatedClass", "add",bigController.TestModify());
    }


    @PostMapping("/addcommand")
    public ConcurrentMap<String, String> addCommand(@RequestBody Map<String, String> request) {
        UpdatedClass.add(request.get("name"), request.get("name"));
        return UpdatedClass.returnMap();
    }


    @PostMapping("/deletecommand")
    public ConcurrentMap<String, String> deleteCommand(@RequestBody Map<String, String> request) throws NoSuchFieldException {

        UpdatedClass.delete(request.get("name"));
        return UpdatedClass.returnMap();

    }


}
