package com.hubstaffmicroservices.tracktime;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class UpdatedClass {

    public static ConcurrentMap<String, String> cmdMap;

    public static void instantiate() {
        cmdMap = new ConcurrentHashMap<>();
        cmdMap.put("add", "add");
        cmdMap.put("subtract", "subtract");
        cmdMap.put("multiply", "multiply");
    }

    public int i;

    public int a;

    public int c;
    public int b;

//    public int add(int a, int b) {
//        return a + b;
//    }

    public static void delete(String cmd) {
        cmdMap.remove(cmd);
    }

    public static void add(String cmd, String value) {
        cmdMap.put(cmd, value);
    }

    public static ConcurrentMap<String,String > returnMap()
    {
        return cmdMap;
    }


}
