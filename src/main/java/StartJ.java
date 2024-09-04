/*package com.test.appium;
import java.io.File;
import java.io.IOException;

public class StartJ {

	public static void main( String[] args ) {
        try {
            // Start Android AVD
            startEmulator();

           
            // Start MuMu Player
            startMuMuPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startEmulator() throws IOException {
        // Path to the emulator executable
        File emulatorDir = new File("C:\\Users\\admin_10\\AppData\\Local\\Android\\Sdk\\emulator");
        String emulatorCommand = "emulator -avd MuMuPlayerGlobal-12.0-0 -netdelay none -netspeed full";

        ProcessBuilder emulatorProcessBuilder = new ProcessBuilder("cmd.exe", "/c", emulatorCommand);
        emulatorProcessBuilder.directory(emulatorDir);
        Process emulatorProcess = emulatorProcessBuilder.start();
        // Optionally, you can read the emulator process output here
        // Note: To avoid the process hanging indefinitely, you might want to handle the input stream.
    }

    private static void startMuMuPlayer() throws IOException {
        // Path to the MuMu Player executable
        File muMuPlayerDir = new File("C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell");
        String muMuPlayerCommand = "start MuMuPlayer.exe";

        ProcessBuilder muMuPlayerProcessBuilder = new ProcessBuilder("cmd.exe", "/c", muMuPlayerCommand);
        muMuPlayerProcessBuilder.directory(muMuPlayerDir);
        Process muMuPlayerProcess = muMuPlayerProcessBuilder.start();
                                                                                                          
    }
}
*/