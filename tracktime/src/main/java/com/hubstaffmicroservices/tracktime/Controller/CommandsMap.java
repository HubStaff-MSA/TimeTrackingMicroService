package com.hubstaffmicroservices.tracktime.Controller;

import com.hubstaffmicroservices.tracktime.Commands.LastCommand;
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
        cmdMap.put("get_timesheet" , getTimeSheetCommand.class);
    }




    public int b;

    public int i;


//    public int add(int a, int b) {
//        return a + b;
//    }

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
}
