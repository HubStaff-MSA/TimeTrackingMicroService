package com.hubstaffmicroservices.tracktime;

import com.hubstaffmicroservices.tracktime.Controller.MyClassLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class UpdatedClass {

    public static ConcurrentMap<String, Class<?>> cmdMap;

    public static void instantiate() {
        cmdMap = new ConcurrentHashMap<>();
//        cmdMap.put("add", addCommand.class);
//        cmdMap.put("subtract", );
//        cmdMap.put("multiply", "multiply");
    }

    public int i;

    public int a;

    public int b;

//    public int add(int a, int b) {
//        return a + b;
//    }

    public static void delete(String cmd) {
        cmdMap.remove(cmd);
    }

    public static ConcurrentMap<String,Class<?>> add(String cmd) throws IOException {
        String ClassName = "com.hubstaffmicroservices.tracktime.Commands." + cmd;
        byte[] bytes = Files.readAllBytes(Paths.get("tracktime/target/classes/com/hubstaffmicroservices/tracktime/Commands/"+cmd + ".class"));
        MyClassLoader loader = new MyClassLoader();
        Class<?> newCommand = loader.loadClass(bytes, ClassName);
        cmdMap.put(cmd, newCommand);
        return cmdMap;
    }

//    public static void add(String cmd, String value) {
//        cmdMap.put(cmd, value);
//    }
//
    public static ConcurrentMap<String, Class<?>> returnMap()
    {
        return cmdMap;
    }


}
