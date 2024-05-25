//package com.hubstaffmicroservices.tracktime.Controller;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class myFileWatcher {
//
//    private static final String JAVA_FILE_EXTENSION = ".java";
//    private String mavenCommand;
//    private Path directory;
//    private WatchService watchService;
//    private Map<Path, Long> fileTimestamps;
//
//    public myFileWatcher() {
//        this.mavenCommand = "mvn compile";
//        this.directory = Paths.get("tracktime", "src", "main", "java", "com", "hubstaffmicroservices", "tracktime", "Commands");
//        this.fileTimestamps = new HashMap<>();
//    }
//
//    @PostConstruct
//    public void watchJavaFiles() {
//        try {
//            // Create a WatchService
//            watchService = FileSystems.getDefault().newWatchService();
//
//            // Register the directory for watch with the service
//            directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
//
//            // Infinite loop to continuously watch for file changes
//            while (true) {
//                // Wait for events
//                WatchKey key = watchService.take();
//
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    // Get the event type
//                    WatchEvent.Kind<?> kind = event.kind();
//
//                    // Get the context (file name) of the event
//                    @SuppressWarnings("unchecked")
//                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
//                    Path fileName = ev.context();
//
//                    // Check if the file is a Java file
//                    if (fileName.toString().endsWith(JAVA_FILE_EXTENSION)) {
//                        Path filePath = directory.resolve(fileName);
//
//                        // Check if the file is modified or created
//                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE) {
//                            // Recompile the modified or new file
//                            System.out.println("Java file change detected. Recompiling...");
//                            Process process = Runtime.getRuntime().exec(mavenCommand);
//                            process.waitFor();
//
//                            // Update timestamp for the file
//                            fileTimestamps.put(filePath, System.currentTimeMillis());
//                        }
//                    }
//                }
//
//                // Reset the key
//                key.reset();
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (watchService != null) {
//                    watchService.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
