package com.test.appium;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class App {
	 private static final String VM_PATH = "C:\\Users\\admin_14\\Netease\\MuMuPlayerGlobal-12.0\\vms\\MuMuPlayerGlobal-12.0-0  ";

    private static final String VM_BASE_PATH = "C:\\Users\\admin_14\\Netease\\MuMuPlayerGlobal-12.0\\vms";
    private static final String MUMU_PLAYER_EXECUTABLE = "MuMuPlayer.exe";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            // Get the number of copies to create
            int numberOfCopies = getNumberOfCopies();
            copy(numberOfCopies);

            // Get the specific copies the user wants to start
            List<String> copiesToStart = getCopiesToStart(numberOfCopies);

            // Start the selected copies in separate threads
            Thread[] threads = new Thread[copiesToStart.size()];
            for (int i = 0; i < copiesToStart.size(); i++) {
                final String copy = copiesToStart.get(i);
                threads[i] = new Thread(() -> {
                    try {
                        System.out.println("Starting MuMu Player for: " + copy);
                        startMuMuPlayer(copy);
                    } catch (IOException | InterruptedException e) {
                        System.err.println("Error during MuMu Player start: " + e.getMessage());
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
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
        Path sourceFolder = Paths.get(VM_BASE_PATH, "MuMuPlayerGlobal-12.0-0");

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
                            // Ignore invalid folder names
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

    // Start MuMu Player for a specific instance
    private static void startMuMuPlayer(String copy) throws IOException, InterruptedException {
        String muMuPlayerCommand = "start MuMuPlayer.exe";

        // Set the correct path for each MuMu Player instance
        Path muMuPlayerPath = Paths.get(VM_PATH);
        ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
        muMuPlayerProcessBuilder.directory(muMuPlayerPath.toFile());  // Change the directory to the specific instance

        Process process = muMuPlayerProcessBuilder.start();

        try {
            process.getInputStream().transferTo(System.out);
            process.getErrorStream().transferTo(System.err);
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("MuMu Player process interrupted: " + e.getMessage());
        } finally {
            process.destroy();
        }

        System.out.println("MuMu Player started for: " + copy);
    }
}

    /*private static void startEmulator(String avdName) throws IOException {
        String emulatorCommand = "emulator -avd " + avdName + " -netdelay none -netspeed full";

        ProcessBuilder emulatorProcessBuilder = new ProcessBuilder("cmd.exe", "/c", emulatorCommand);
        emulatorProcessBuilder.directory(new File(EMULATOR_PATH));
        Process process = emulatorProcessBuilder.start();

        try {
            process.getInputStream().transferTo(System.out);
            process.getErrorStream().transferTo(System.err);
            process.waitFor(); // Wait for the process to complete if necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.err.println("Emulator process interrupted: " + e.getMessage());
        } finally {
            process.destroy(); // Ensure the process is properly terminated
        }

        System.out.println("Emulator " + avdName + " started in directory " + EMULATOR_PATH);
    }*/


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

