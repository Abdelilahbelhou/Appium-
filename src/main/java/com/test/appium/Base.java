package com.test.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Base {

    public static void mconfigurationAppium() {
        try {
            // Ensure ADB is connected to MuMu Player first
            connectToADB();

            // Wait for 5 seconds (5000 milliseconds) to ensure the connection is stable
           Thread.sleep(5000); 

            // Set up DesiredCapabilities for Appium
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "MuMu Player 12"); // Name of your emulator
            capabilities.setCapability("udid", "127.0.0.1:7555"); // ADB connection for MuMu Player
            capabilities.setCapability("appPackage", "io.appium.android.apis"); // Package name of the app
            capabilities.setCapability("appActivity", "io.appium.android.apis.ApiDemos"); // Main activity of the app
            capabilities.setCapability("noReset", true); // Retain the app state between sessions
            capabilities.setCapability("automationName", "UiAutomator2"); // Automation engine
            capabilities.setCapability("app", "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell\\resources\\ApiDemos-debug.apk"); // Path to your APK

            // Initialize Appium Driver
            AppiumDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), capabilities);

            //  test code here
            driver.quit();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void connectToADB() throws IOException {
        // Command to connect to ADB
    	
         String command = "adb connect 127.0.0.1:7555" ;
    	
        
		// Execute the command
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        Process process = processBuilder.start();

        // Read command output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the command to complete
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Failed to connect to ADB. Exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        Base base = new Base();
        Base.mconfigurationAppium(); // This starts the entire configuration and testing process
    }*/
}
