package com.test.appium;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class App {

    static final int NUMBER_OF_AVDS = 5; // Set this to the desired number of AVDs
    static final String EMULATOR_PATH = "C:\\Users\\admin_14\\AppData\\Local\\Android\\Sdk\\emulator";
    static final String MUMU_PLAYER_PATH = "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell";

    public static void main(String[] args) throws InterruptedException, IOException {
        Thread[] threads = new Thread[NUMBER_OF_AVDS];

        for (int i = 0; i < NUMBER_OF_AVDS; i++) {
            final int avdIndex = i;
            String avdName = "MuMuPlayerGlobal-12.0-" + i;

            threads[i] = new Thread(() -> {
                try {
                    System.out.println("Starting emulator: " + avdName);
                    startEmulator(avdName);
                    System.out.println("Starting MuMu Player for: " + avdName);
                    startMuMuPlayer();
                } catch (IOException e) {
                    System.err.println("Error during emulator or MuMu Player start: " + e.getMessage());
                }
            });

            threads[i].start(); // Start the thread
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Initialize Appium after the emulators have started
        Base.mconfigurationAppium();
    }

    public static void copy() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of copies you want to create: ");
            int numberOfCopies = scanner.nextInt();

            Path baseDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell");
            Path sourceFolder = baseDirectory.resolve("MuMuPlayerGlobal-12.0-0");

            for (int i = 0; i < numberOfCopies; i++) {
                Path targetDirectory = generateIncrementingTargetDirectory(baseDirectory);
                try {
                    copyFolderAutomatically(sourceFolder, targetDirectory);
                    System.out.println("Folder copied successfully to: " + targetDirectory);
                } catch (IOException e) {
                    System.err.println("Failed to copy folder: " + e.getMessage());
                }
            }
        }
    }

    private static Path generateIncrementingTargetDirectory(Path baseTargetDirectory) {
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
                        } catch (NumberFormatException e) {
                            // Ignore if the part is not a number
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

    private static void startEmulator(String avdName) throws IOException {
        String emulatorCommand = "emulator -avd " + avdName + " -netdelay none -netspeed full";

        ProcessBuilder emulatorProcessBuilder = new ProcessBuilder("cmd.exe", "/c", emulatorCommand);
        emulatorProcessBuilder.directory(new File(EMULATOR_PATH));
        Process process = emulatorProcessBuilder.start();
        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.err);
        System.out.println("Emulator " + avdName + " started in directory " + EMULATOR_PATH);
    }

    private static void startMuMuPlayer() throws IOException {
        String muMuPlayerCommand = "start MuMuPlayer.exe";

        ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
        muMuPlayerProcessBuilder.directory(new File(MUMU_PLAYER_PATH));
        Process process = muMuPlayerProcessBuilder.start();
        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.err);

        System.out.println("MuMu Player started from directory " + MUMU_PLAYER_PATH);
    }
}


/*
        // Define the base directory and source folder
        Path baseDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms");
        Path sourceFolder = baseDirectory.resolve("MuMuPlayerGlobal-12.0-0"); // Replace with the actual source folder name

        // Automatically generate the target directory with an incrementing number
        Path targetDirectory = generateIncrementingTargetDirectory(Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms"));

        try {
            // Automatically copy the folder to the generated target directory
            copyFolderAutomatically(sourceFolder, targetDirectory);
            System.out.println("Folder copied successfully to: " + targetDirectory);
        } catch (IOException e) {
            System.err.println("Failed to copy folder: " + e.getMessage());
        }
    }

    // Method to automatically generate a target directory with an incrementing number
    private static Path generateIncrementingTargetDirectory(Path baseTargetDirectory) {
        // Increment the number
    	//Path generateIncrementingTargetDirectory(Path baseTargetDirectory1) {
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
        // Walk through the file tree and copy each file/directory
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
*/
  
   /* public static void  Copy() {

        
            // Define the base directory and source folder
            Path baseDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms");
            Path sourceFolder = baseDirectory.resolve("MuMuPlayerGlobal-12.0-base"); // Replace with the actual source folder name

            // Automatically generate the target directory with a unique name
            Path targetDirectory = generateUniqueTargetDirectory(Paths.get("C:\\\\Program Files\\\\Netease\\\\MuMuPlayerGlobal-12.0\\\\vms"));

            try {
                // Automatically copy the folder to the generated target directory
                copyFolderAutomatically(sourceFolder, targetDirectory);
                System.out.println("Folder copied successfully to: " + targetDirectory);
            } catch (IOException e) {
                System.err.println("Failed to copy folder: " + e.getMessage());
            }
        }

        // Method to automatically generate a unique target directory
        private static Path generateUniqueTargetDirectory(Path baseTargetDirectory) {
            // Generate a unique folder name using the current timestamp
            String  timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path uniqueTargetDirectory = baseTargetDirectory.resolve("MuMuPlayerGlobal-12.0-_" + timestamp);

            try {
                // Create the directory if it does not exist
                Files.createDirectories(uniqueTargetDirectory);
            } catch (IOException e) {
                System.err.println("Failed to create target directory: " + e.getMessage());
            }

            return uniqueTargetDirectory;
        }

        // Method to automatically copy a folder and its contents
        public static void copyFolderAutomatically(Path source, Path target) throws IOException {
            // Walk through the file tree and copy each file/directory
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
	
*/

