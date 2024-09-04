/*package com.test.appium;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartJ3 {

    public static void main(String[] args) {
        try {
            // Generate a new AVD name or use the next available port
            String newAvdName = generateNewAvdName();

            // Start Android AVD
            startEmulator(newAvdName);

            // Start MuMu Player
            startMuMuPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startEmulator(String avdName) throws IOException {
        // Path to the emulator executable
        String userHome = System.getProperty("user.home");
        File emulatorDir = new File(userHome, "AppData\\Local\\Android\\Sdk\\emulator");

        // Use a new port if needed
        int port = findNextAvailablePort(5554); // Default emulator port starts at 5554
        String emulatorCommand = "emulator -avd " + avdName + " -port " + port + " -netdelay none -netspeed full";

        ProcessBuilder emulatorProcessBuilder = new ProcessBuilder("cmd.exe", "/c", emulatorCommand);
        emulatorProcessBuilder.directory(emulatorDir);
        Process emulatorProcess = emulatorProcessBuilder.start();
        // Optionally, you can read the emulator process output here
    }

    private static String generateNewAvdName() {
        // Base name for the AVD
        String baseAvdName = "MuMuPlayerGlobal-12.0-base";
        String userHome = System.getProperty("user.home");
        File avdDir = new File(userHome, ".android\\avd");

        int maxNumber = 0;

        if (avdDir.exists() && avdDir.isDirectory()) {
            File[] files = avdDir.listFiles((dir, name) -> name.startsWith(baseAvdName) && name.endsWith(".avd"));

            if (files != null) {
                Pattern pattern = Pattern.compile(baseAvdName + "(\\d+)\\.avd");

                for (File file : files) {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.matches()) {
                        int currentNumber = Integer.parseInt(matcher.group(1));
                        if (currentNumber > maxNumber) {
                            maxNumber = currentNumber;
                        }
                    }
                }
            }
        }

        // Increment the highest number found
        int newNumber = maxNumber + 1;

        // Return the new AVD name
        return baseAvdName + newNumber;
    }

    private static int findNextAvailablePort(int startingPort) throws IOException {
        // List all currently running ADB devices
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "adb devices");
        Process process = processBuilder.start();

        // Read the output of the command
        List<String> outputLines = new ArrayList<>();
        try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
            }
        }

        // Extract the port numbers in use 
        List<Integer> usedPorts = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{4,5})\\s+device");

        for (String line : outputLines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                usedPorts.add(Integer.parseInt(matcher.group(1)));
            }
        }

        // Find the next available port
        int port = startingPort;
        while (usedPorts.contains(port) || usedPorts.contains(port + 1)) {
            port += 2; // Android emulators use two consecutive ports, e.g., 5554 & 5555
        }

        return port;
    }

    private static void startMuMuPlayer() throws IOException {
        // Attempt to locate MuMu Player in standard installation directories
        String[] possiblePaths = {
            "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell\\",
            "C:\\Program Files (x86)\\Netease\\MuMuPlayerGlobal-12.0\\shell",
            System.getProperty("user.home") + "\\AppData\\Local\\Netease\\MuMuPlayerGlobal-12.0\\shell"
        };
       
        File muMuPlayerDir = null;

        for (String path : possiblePaths) {
            File dir = new File(path);
            if (dir.exists() && new File(dir, "MuMuPlayer.exe").exists()) {
                muMuPlayerDir = dir;
                break;
            }
        }

        if (muMuPlayerDir == null) {
            throw new IOException("MuMu Player executable not found in standard directories.");
        }

        String muMuPlayerCommand = "start MuMuPlayer.exe";
        ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
        muMuPlayerProcessBuilder.directory(muMuPlayerDir);
        Process muMuPlayerProcess = muMuPlayerProcessBuilder.start();
    }
}
*/