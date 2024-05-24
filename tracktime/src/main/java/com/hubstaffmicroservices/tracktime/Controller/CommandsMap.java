package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.Commands.LastCommand;
import com.hubstaffmicroservices.tracktime.Commands.ListOfEmployeesTrackTimeCommand;
import com.hubstaffmicroservices.tracktime.Commands.ListOfTimeTracksByUsersIds;
import com.hubstaffmicroservices.tracktime.Commands.getTimeSheetCommand;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandsMap {

    public static ConcurrentMap<String, Class<?>> cmdMap;

    @PostConstruct
    public static void instantiate() {
        cmdMap = new ConcurrentHashMap<>();
        cmdMap.put("add", LastCommand.class);
        cmdMap.put("getTimeSheetCommand" , getTimeSheetCommand.class);
        cmdMap.put("ListOfTimeTracksByUsersIds", ListOfTimeTracksByUsersIds.class);
        cmdMap.put("ListOfEmployeesTrackTimeCommand", ListOfEmployeesTrackTimeCommand.class);
    }


    public static ConcurrentMap<String,Class<?>> delete(String cmd) {
        cmdMap.remove(cmd);
        Path path = Paths.get("tracktime/src/main/java/com/hubstaffmicroservices/tracktime/Commands/"+ cmd + ".java");
        try {
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File not found, so it couldn't be deleted.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while trying to delete the file.");
            e.printStackTrace();
        }
        return cmdMap;
    }

    public static ConcurrentMap<String,Class<?>> add(String cmd) throws IOException {
        String ClassName = "com.hubstaffmicroservices.tracktime.Commands." + cmd;
        byte[] bytes = Files.readAllBytes(Paths.get("tracktime/target/classes/com/hubstaffmicroservices/tracktime/Commands/"+cmd + ".class"));
        MyClassLoader loader = new MyClassLoader();
        Class<?> newCommand = loader.loadClass(bytes, ClassName);
        cmdMap.put(cmd, newCommand);
        return cmdMap;
    }


    public static ConcurrentMap<String, Class<?>> returnMap()
    {
        return cmdMap;
    }


}
