package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.Commands.LastCommand;

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
    public String freezeApplication() throws SQLException {
        freezeConfig.setIsFrozen(true);
        return "Application frozen";
    }

    @PostMapping("/continue")
    public String unfreezeApplication() throws SQLException {
        freezeConfig.setIsFrozen(false);
        return "Application unfrozen";
    }

    @PostMapping("/update")
    public String updateTrackTime() throws IOException {
        return bigController.replace("com.hubstaffmicroservices.tracktime.Controller.UpdatedClass", "add",bigController.TestModify());
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


    @GetMapping("/getFields")
    public String getFields() {
        String fieldsString = "";
        Field[] fields = CommandsMap.class.getFields();
        for (Field field : fields) {
            fieldsString += field.getName() + " ";
        }
        return fieldsString;
    }

    @PostMapping("/updatecommand")
    public ConcurrentMap<String, Class<?>> updateCommand(@RequestBody Map<String, String> request) throws NoSuchFieldException {

        CommandsMap.update(request.get("name"));
        return CommandsMap.returnMap();

    }

    @PostMapping("/testupdatecommand")
    public int testUpdateCommand(@RequestBody Map<String, Integer> request)  {

        var c = LastCommand.add(request.get("a") , request.get("b"));
        return c;
    }

}
