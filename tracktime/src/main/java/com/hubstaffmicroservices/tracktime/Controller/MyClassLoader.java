package com.hubstaffmicroservices.tracktime.Controller;

import static org.springframework.cglib.core.ReflectUtils.defineClass;

public class MyClassLoader extends ClassLoader {
    public Class<?> loadClass(byte[] byteCode, String className) {
        return defineClass(className, byteCode, 0, byteCode.length);
    }
}
