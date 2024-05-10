package com.hubstaffmicroservices.tracktime.Controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//@Component
public class DynamicClassCompiler implements ApplicationRunner {

    private final String projectDirectory = "tracktime/src/main/java/com/hubstaffmicroservices/tracktime";
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

//    @Override
    public void run(ApplicationArguments args) {
        // Initial compilation
        compileJavaFiles();

        // Watch for changes
        watchForChanges();
    }

    private void compileJavaFiles() {
        File[] javaFiles = new File(projectDirectory)
                .listFiles((dir, name) -> name.endsWith(".java"));

        if (javaFiles != null && javaFiles.length > 0) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            if (compiler != null) {
                for (File javaFile : javaFiles) {
                    int compilationResult = compiler.run(null, null, null, javaFile.getAbsolutePath());

                    if (compilationResult == 0) {
                        System.out.println("Compiled: " + javaFile.getName());
                    } else {
                        System.err.println("Compilation failed for: " + javaFile.getName());
                    }
                }
            } else {
                System.err.println("Java compiler not found. Make sure you're running in a JDK environment.");
            }
        } else {
            System.err.println("No Java files found in the project directory.");
        }
    }

    private void watchForChanges() {
        executorService.scheduleAtFixedRate(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(projectDirectory);
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (filename.toString().endsWith(".java")) {
                        System.out.println("Changes detected in: " + filename);
                        compileJavaFiles();
                    }
                }
                key.reset();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
