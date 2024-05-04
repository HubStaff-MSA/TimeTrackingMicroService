package com.hubstaffmicroservices.tracktime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@Data
@RequiredArgsConstructor
public class BigController extends ClassLoader{

    public String replace(String className, String commandName, String byteString){

        byte[] byteArray = Base64.getDecoder().decode(byteString);
        Class<?> newCommand = this.loadClass(byteArray, className);
//        cmdMap.put(commandName, newCommand);
        return  byteArray.toString();
    }



    public String TestModify() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("tracktime/src/main/java/com/hubstaffmicroservices/tracktime/UpdatedClass.java"));
        String byteString = Base64.getEncoder().encodeToString(bytes);
//        System.out.println(byteString);
        return byteString;
    }


    public Class<?> loadClass(byte[] byteCode, String className) {
        return defineClass(className, byteCode, 0, byteCode.length);

    }

}
