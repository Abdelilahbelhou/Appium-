/*package com.test.appium;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.appium.java_client.android.AndroidDriver;

public class App {

    static AndroidDriver driver = null;

    public static void main(String[] args) throws InterruptedException, IOException {
        // Specify the number of AVDs to start
        int numberOfAVDs = 5; // Set this to the desired number of AVDs

        // Create a fixed thread pool with the number of AVDs
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfAVDs);

        // Automatically generate and start each AVD using threads
        for (int i = 0; i < numberOfAVDs; i++) {
            String avdName = "MuMuPlayerGlobal-12.0-" + i;
            
            // Submit each emulator start task to the thread pool
            executorService.submit(() -> {
                try {
                    copy();  // Copy the AVD folder
                    startEmulator(avdName);  // Start the emulator
                    startMuMuPlayer();  // Start MuMu Player
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor service
        executorService.shutdown();

        // Wait for the emulators to start (adjust based on your needs)
        while (!executorService.isTerminated()) {
            Thread.sleep(5000);
        }

        // Initialize Appium after the emulators have started
        Base.mconfigurationAppium();
    }

    public static void copy() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of copies you want to create: ");
        int numberOfCopies = scanner.nextInt();

        // Define the base directory and source folder
        Path baseDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms");
        Path sourceFolder = baseDirectory.resolve("MuMuPlayerGlobal-12.0-base"); // Replace with the actual source folder name

        // Create the specified number of copies
        for (int i = 0; i < numberOfCopies; i++) {
            Path targetDirectory = generateIncrementingTargetDirectory(baseDirectory);
            try {
                copyFolderAutomatically(sourceFolder, targetDirectory);
                System.out.println("Folder copied successfully to: " + targetDirectory);
            } catch (IOException e) {
                System.err.println("Failed to copy folder: " + e.getMessage());
            }
        }

        scanner.close();
    }

    // Method to automatically generate a target directory with an incrementing number
    private static Path generateIncrementingTargetDirectory(Path baseTargetDirectory) {
        int highestNumber = 0;

        try {
            if (Files.exists(baseTargetDirectory)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(baseTargetDirectory)) {
                    for (Path path : directoryStream) {
                        if (Files.isDirectory(path)) {
                            String folderName = path.getFileName().toString();
                            String[] parts = folderName.split("-");
                            if (parts.length > 2) {
                                try {
                                    int number = Integer.parseInt(parts[2]);
                                    if (number > highestNumber) {
                                        highestNumber = number;
                                    }
                                } catch (NumberFormatException e) {
                                    // Ignore if the part is not a number
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to scan directories: " + e.getMessage());
        }

        int newNumber = highestNumber + 1;
        Path uniqueTargetDirectory = baseTargetDirectory.resolve("MuMuPlayerGlobal-12.0-" + newNumber);

        try {
            Files.createDirectories(uniqueTargetDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create target directory: " + e.getMessage());
        }

        return uniqueTargetDirectory;
    }

    // Method to automatically copy a folder and its contents
    public static void copyFolderAutomatically(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (Files.notExists(targetDir)) {
                    Files.createDirectory(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void startEmulator(String avdName) throws IOException, InterruptedException {
        // Path to the emulator executable
        File emulatorDir = new File("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms\\MuMuPlayerGlobal-12.0-base");
        String emulatorCommand = "emulator -avd " + avdName + " -netdelay none -netspeed full";

        ProcessBuilder emulatorProcessBuilder = new ProcessBuilder("cmd.exe", "/c", emulatorCommand);
        emulatorProcessBuilder.directory(emulatorDir);
        emulatorProcessBuilder.start();
        
        // Add a delay to ensure emulator has time to start
        Thread.sleep(10000);
    }

    private static void startMuMuPlayer() throws IOException, InterruptedException {
        // Path to the MuMu Player executable
        File muMuPlayerDir = new File("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell");
        String muMuPlayerCommand = "start MuMuPlayer.exe";

        ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
        muMuPlayerProcessBuilder.directory(muMuPlayerDir);
        muMuPlayerProcessBuilder.start();
        
        // Add a delay to ensure MuMu Player has time to start
        Thread.sleep(5000);
    }
}*/
