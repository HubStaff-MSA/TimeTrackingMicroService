package com.hubstaffmicroservices.tracktime.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BigController {

    public String replace(String className, String commandName, String byteString){

        byte[] byteArray = Base64.getDecoder().decode(byteString);
        MyClassLoader loader = new MyClassLoader();
        Class<?> newCommand = loader.loadClass(byteArray, className);
//        cmdMap.put(commandName, newCommand);
        Field[] fields = newCommand.getFields();
        String fieldsString = "";
        for (Field field : fields) {
            fieldsString += field.getName() + " ";
        }


        return  fieldsString;
    }



    public String TestModify() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("tracktime/target/classes/com/hubstaffmicroservices/tracktime/UpdatedClass.class"));
        String byteString = Base64.getEncoder().encodeToString(bytes);
//        System.out.println(byteString);
        return byteString;
    }


}
