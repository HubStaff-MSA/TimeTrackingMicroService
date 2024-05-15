package com.hubstaffmicroservices.tracktime.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Live Reload and execution of another java file.
 * The other Java file needs to implements the Supplier interface.
 * Public domain code.
 *
 * @author Anthony Goubard - japplis.com
 */
public class Reloader {
    private String fileName;

    public Reloader(String fileName) {
        this.fileName = fileName;
        try {
            monitorForReload();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void monitorForReload() throws IOException {
        Path file = Path.of(fileName);
        if (Files.notExists(file)) throw new FileNotFoundException(file + " not found");
        if (!file.toString().endsWith(".java")) throw new IllegalArgumentException(file + " is not a .java file");
        Runnable compileAndRun = () -> { // statement executed each time the file is saved
            try {
                boolean compiled = compile(file);
                if (compiled) execute(file);
            } catch (Exception ex) {
                System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        };
        compileAndRun.run(); // First execution at launch
        monitorFile(file, compileAndRun);
    }

    private boolean compile(Path file) throws IOException, InterruptedException {
        String javaHome = System.getenv("JAVA_HOME");
        String javaCompiler = javaHome + File.separator + "bin" + File.separator + "javac";
        Process compilation = Runtime.getRuntime().exec(new String[] {javaCompiler, file.toString()});
        compilation.getErrorStream().transferTo(System.err); // Only show compilation errors
        compilation.waitFor(1, TimeUnit.MINUTES); // Wait for max 1 minute for the compilation
        boolean compiled = compilation.exitValue() == 0;
        if (!compiled) System.err.println("Compilation failed");
        return compiled;
    }

    private void execute(Path file) throws Exception {
        // Execution is done in a separate class loader that doesn't use the current class loader as parent
        URL classpath = file.toAbsolutePath().getParent().toUri().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[] { classpath }, ClassLoader.getPlatformClassLoader());
        String supplierClassName = file.toString().substring(file.toString().lastIndexOf(File.separator) + 1, file.toString().lastIndexOf("."));
        // Create a new instance of the supplier with the class loader and call the get method
        Class<?> stringSupplierClass = Class.forName(supplierClassName, true, loader);
        Object stringSupplier = stringSupplierClass.getDeclaredConstructor().newInstance();
        if (stringSupplier instanceof Supplier) {
            Object newResult = ((Supplier) stringSupplier).get();
            System.out.println("> " + newResult);
        }
    }

    public static void monitorFile(Path file, Runnable onFileChanged) throws IOException {
        // Using WatchService was more code and less reliable
        Runnable monitor = () -> {
            try {
                long lastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
                while (Files.exists(file)) {
                    long newLastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
                    if (lastModified != newLastModified) {
                        lastModified = newLastModified;
                        onFileChanged.run();
                    }
                    Thread.sleep(1_000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        Thread monitorThread = new Thread(monitor, file.toString());
        monitorThread.start();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Missing file to execute argument.");
            System.exit(-1);
        }
        new Reloader(args[0]);
    }
}
