package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.Commands.LastCommand;
import com.hubstaffmicroservices.tracktime.UpdatedClass;

import com.hubstaffmicroservices.tracktime.TrackTime;
import com.hubstaffmicroservices.tracktime.TrackTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
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
    public ConcurrentMap<String, Class<?>> addCommand(@RequestBody Map<String, String> request) throws IOException {
        try {
            UpdatedClass.add(request.get("name"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UpdatedClass.returnMap();
    }
    @PostMapping("/deletecommand")
    public ConcurrentMap<String, Class<?>> deleteCommand(@RequestBody Map<String, String> request) throws IOException {
        UpdatedClass.delete(request.get("name"));
        return UpdatedClass.returnMap();
    }


    @GetMapping("/getFields")
    public String getFields() {
        String fieldsString = "";
        Field[] fields = UpdatedClass.class.getFields();
        for (Field field : fields) {
            fieldsString += field.getName() + " ";
        }
        return fieldsString;
    }

    @PostMapping("/updatecommand")
    public ConcurrentMap<String, Class<?>> updateCommand(@RequestBody Map<String, String> request) throws NoSuchFieldException {

        UpdatedClass.update(request.get("name"));
        return UpdatedClass.returnMap();

    }

    @PostMapping("/testupdatecommand")
    public int testUpdateCommand(@RequestBody Map<String, Integer> request)  {

        var c = LastCommand.add(request.get("a") , request.get("b"));
        return c;
    }




}
