package com.test.appium;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String VM_PATH = "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell";
    private static final String VM_BASE_PATH = "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms";
    private static final String MUMU_PLAYER_EXECUTABLE = "MuMuPlayer.exe";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            // Get the number of copies to create
            int numberOfCopies = getNumberOfCopies();
            copy(numberOfCopies);

            // Get the specific copies the user wants to start
            List<String> copiesToStart = getCopiesToStart(numberOfCopies);

            // Start the selected copies in separate threads using Thread class
            List<Thread> threads = new ArrayList<>();
            for (String copy : copiesToStart) {
                Thread thread = new Thread(new MuMuPlayerRunnable(copy));
                
                threads.add(thread);
                thread.start();  // Start the thread
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();  // Ensure all threads complete execution
            }

            // Initialize Appium after the instances have started
            Base.mconfigurationAppium();

        } catch (InterruptedException e) {
            System.err.println("Error in main execution: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Get the number of copies from the user
    private static int getNumberOfCopies() {
        System.out.print("Enter the number of copies you want to create: ");
        return scanner.nextInt();
    }

    // Get the specific copies to start
    private static List<String> getCopiesToStart(int numberOfCopies) {
        scanner.nextLine(); // Consume leftover newline from previous input
        System.out.print("Enter the copy numbers you want to start (comma-separated, e.g., -1, -2): ");
        String[] selectedCopies = scanner.nextLine().split(",");

        List<String> validCopies = new ArrayList<>();
        Path baseDirectory = Paths.get(VM_BASE_PATH);

        for (String copy : selectedCopies) {
            copy = copy.trim();
            if (copy.matches("-\\d{1}")) {
                String fullCopyName = "MuMuPlayerGlobal-12.0" + copy;
                Path targetDirectory = baseDirectory.resolve(fullCopyName);

                if (Files.exists(targetDirectory) && Files.isDirectory(targetDirectory)) {
                    validCopies.add(fullCopyName);
                } else {
                    System.err.println("Copy does not exist in the folder: " + fullCopyName);
                }
            } else {
                System.err.println("Invalid copy format: " + copy);
            }
        }

        return validCopies;
    }

    // Copy the MuMuPlayer folders
    private static void copy(int numberOfCopies) {
        Path sourceFolder = Paths.get(VM_BASE_PATH, "MuMuPlayerGlobal-12.0-1");

        for (int i = 0; i < numberOfCopies; i++) {
            Path targetDirectory = generateIncrementingTargetDirectory(Paths.get(VM_BASE_PATH));
            try {
                copyFolderAutomatically(sourceFolder, targetDirectory);
                System.out.println("Folder copied successfully to: " + targetDirectory);
            } catch (IOException e) {
                System.err.println("Failed to copy folder: " + e.getMessage());
            }
        }
    }

    // Generate incrementing folder paths
    private static Path generateIncrementingTargetDirectory(Path baseTargetDirectory) {
        int highestNumber = findHighestDirectoryNumber(baseTargetDirectory);
        int newNumber = highestNumber + 1;
        Path uniqueTargetDirectory = baseTargetDirectory.resolve("MuMuPlayerGlobal-12.0-" + newNumber);

        try {
            Files.createDirectories(uniqueTargetDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create target directory: " + e.getMessage());
        }

        return uniqueTargetDirectory;
    }

    // Find the highest numbered directory in the target path
    private static int findHighestDirectoryNumber(Path baseTargetDirectory) {
        int highestNumber = 0;

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
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to scan directories: " + e.getMessage());
        }

        return highestNumber;
    }

    // Copy folder and its contents
    private static void copyFolderAutomatically(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if (Files.notExists(targetDir)) {
                    Files.createDirectory(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    // Runnable to start MuMu Player for a specific instance
    private static class MuMuPlayerRunnable implements Runnable {
        private final String copy;

        public MuMuPlayerRunnable(String copy) {
            this.copy = copy;
        }

        @Override
        public void run() {
            try {
                System.out.println("Starting MuMu Player for: " + copy);
                String muMuPlayerCommand = "start " + MUMU_PLAYER_EXECUTABLE;
                Path muMuPlayerPath = Paths.get(VM_PATH);
                //Path muPlayerPath = Paths.get(VM_BASE_PATH );
                ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
                muMuPlayerProcessBuilder.directory(muMuPlayerPath.toFile());

                Process process = muMuPlayerProcessBuilder.start();
                process.getInputStream().transferTo(System.out);
                process.getErrorStream().transferTo(System.err);
                process.waitFor();
                System.out.println("MuMu Player started for: " + copy);
            } catch (IOException | InterruptedException e) {
                System.err.println("Error during MuMu Player start: " + e.getMessage());
            }
        }
    }
}
