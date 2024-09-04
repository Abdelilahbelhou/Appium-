/*package com.test.appium;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class StartJ4 {

    public static void copy() {
        // Define the base directory, source folder, and target directory
        Path baseDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms\\MuMuPlayerGlobal-12.0-base");
        Path sourceFolder = baseDirectory.resolve("MuMuPlayerGlobal-12.0-base"); // Replace with the actual source folder name
        Path targetDirectory = Paths.get("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\vms");

        try {
            // Automatically copy the folder from the base directory
            copyFolderAutomatically(sourceFolder, targetDirectory);
            System.out.println("Folder copied successfully.");
        } catch (IOException e) {
            System.err.println("Failed to copy folder: " + e.getMessage());
        }
    }

    // Method to automatically copy a folder and its contents
    public static void copyFolderAutomatically(Path source, Path target) throws IOException {
        // If the target directory doesn't exist, create it
        if (Files.notExists(target)) {
            Files.createDirectories(target);
        }

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
}*/
