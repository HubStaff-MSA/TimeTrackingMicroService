package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.Models.TrackTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

public class UpdateTemp {
    public static void update(String cmd) {
        String javaFilePath = "tracktime/src/main/java/com/hubstaffmicroservices/tracktime/Commands/"+cmd+".java"; // Path to your .java file
        String targetDir = "tracktime/target/classes/com/hubstaffmicroservices/tracktime/Commands";

        // Compile the java file
        compileJavaFile(javaFilePath, targetDir);
    }
    public static void compileJavaFile(String javaFilePath, String targetDir) {
        // Get the Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // Check if the Java compiler is available
        if (compiler == null) {
            System.out.println("Java compiler not found. Make sure you're using a JDK.");
            return;
        }

        // Compile the .java file
        int compilationResult = compiler.run(null, null, null, javaFilePath);

        // Check compilation result
        if (compilationResult == 0) {
            System.out.println("Compilation successful");

            // Get the name of the compiled class
            String className = Paths.get(javaFilePath).getFileName().toString().replace(".java", ".class");

            // Construct the path to the compiled class file
            String compiledClassPath = Paths.get(targetDir, className).toString();

            // Replace the existing .class file with the newly compiled one
            replaceClassFile(compiledClassPath, targetDir);
        } else {
            System.out.println("Compilation failed");
        }
    }

    public static void replaceClassFile(String compiledClassPath, String targetDir) {
        try {
            // Read the bytes of the new .class file
            byte[] newClassBytes = Files.readAllBytes(Paths.get(compiledClassPath));

            // Construct the path to the existing .class file
            String existingClassPath = Paths.get(targetDir, Paths.get(compiledClassPath).getFileName().toString()).toString();

            // Replace the existing .class file with the new one
            Files.write(Paths.get(existingClassPath), newClassBytes);

            System.out.println("Class file replaced successfully");
        } catch (IOException e) {
            System.out.println("Error replacing class file: " + e.getMessage());
        }
    }

//
//    @PostMapping("/testupdatecommand")
//    public int testUpdateCommand(@RequestBody Map<String, Integer> request)  {
//
//        var c = LastCommand.add(request.get("a") , request.get("b"));
//        return c;
//    }

    @GetMapping("/getFields")
    public String getFields() {
        String fieldsString = "";
        Field[] fields = CommandsMap.class.getFields();
        for (Field field : fields) {
            fieldsString += field.getName() + " ";
        }
        return fieldsString;
    }

//    @PostMapping("/addCommand")
//    public Queue<TrackTime> addCommandSaveTrackTime() {
//        return trackTimeService.addCommand();
//    }
//
//
//    @PostMapping("/update")
//    public String updateTrackTime() throws IOException {
//        return bigController.replace("com.hubstaffmicroservices.tracktime.Controller.UpdatedClass", "add",bigController.TestModify());
//    }

//    @PostMapping("/updatecommand")
//    public ConcurrentMap<String, Class<?>> updateCommand(@RequestBody Map<String, String> request) throws NoSuchFieldException {
//
//        CommandsMap.update(request.get("name"));
//        return CommandsMap.returnMap();
//
//    }
}
