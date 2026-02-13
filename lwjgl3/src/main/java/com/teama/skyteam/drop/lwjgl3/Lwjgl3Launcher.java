package com.teama.skyteam.drop.lwjgl3;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.teama.skyteam.drop.MainBk;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MainBk(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Sky Team");

        // Enable logging to console //TODO: remove button line
        System.setProperty("org.lwjgl.util.Debug", "true");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setResizable(false);

        int displayModeOption = 1;
        Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
        switch (displayModeOption) {
            case 1: // window 1920x1080
                configuration.setWindowedMode(1920, 1080);
                break;

            case 2: // fullscreen 1920x1080 on the second monitor
                if (monitors.length > 1) {
                    Monitor secondMonitor = monitors[1];
                    DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode(secondMonitor);
                    configuration.setFullscreenMode(displayMode);
                } else {
                    DisplayMode defaultDisplayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
                    configuration.setFullscreenMode(defaultDisplayMode);
                }
                break;

            case 3: // fullscreen 1920x1080 on the default monitor
            default:
                DisplayMode defaultDisplayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
                configuration.setFullscreenMode(defaultDisplayMode);
                break;
        }

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        configuration.setWindowIcon("plane128.png", "plane64.png", "plane32.png", "plane16.png");
        return configuration;
    }
}
