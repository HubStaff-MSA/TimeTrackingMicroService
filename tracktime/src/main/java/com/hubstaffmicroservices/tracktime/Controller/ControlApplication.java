package com.hubstaffmicroservices.tracktime.Controller;


import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import com.hubstaffmicroservices.tracktime.Services.TrackTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/v2/control")
@RequiredArgsConstructor
public class ControlApplication {

    private final TrackTimeService trackTimeService;

    private final FreezeConfig freezeConfig;

    private final BigController bigController;

//    private final DatabaseConfig databaseConfig;
//
//    @PostMapping("/maxConnections")
//    public String updateMaxConnections(@RequestBody int newMaxConnections) {
//        databaseConfig.updateMaxDbConnectionsCount(newMaxConnections);
//        return "Max connections updated to " + newMaxConnections;
//    }
    @PostMapping("/freeze")
    public String freezeApplication() throws SQLException {
        freezeConfig.setIsFrozen(true);
        return "Application frozen";
    }

    @PostMapping("/continue")
    public String unfreezeApplication() throws SQLException {
        freezeConfig.setIsFrozen(false);
        return "Application unfrozen";
    }

    @PostMapping("/addcommand")
    public ConcurrentMap<String, Class<?>> addCommand(@RequestBody Map<String, String> request) throws IOException {
        try {
            CommandsMap.add(request.get("name"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommandsMap.returnMap();
    }
    @PostMapping("/deletecommand")
    public ConcurrentMap<String, Class<?>> deleteCommand(@RequestBody Map<String, String> request) throws IOException {
        CommandsMap.delete(request.get("name"));
        return CommandsMap.returnMap();
    }
}
