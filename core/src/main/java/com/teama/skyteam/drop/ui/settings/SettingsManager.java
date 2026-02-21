package com.teama.skyteam.drop.ui.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {

    private static final String PREFS_NAME = "SkyTeamSettings";

    public static void applySettings() {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) return;
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        if (prefs.getBoolean("fullscreen", false)) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            int w = prefs.getInteger("width", 1920);
            int h = prefs.getInteger("height", 1080);
            Gdx.graphics.setWindowedMode(w, h);
        }
    }

    public static void saveAndApply(int width, int height, boolean fullscreen) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putBoolean("fullscreen", fullscreen);
        prefs.putInteger("width", width);
        prefs.putInteger("height", height);
        prefs.flush();
        applySettings();
    }

    public static boolean isFullscreen() {
        return Gdx.graphics.isFullscreen();
    }

    public static int getSavedWidth() {
        return Gdx.app.getPreferences(PREFS_NAME).getInteger("width", 1920);
    }

    public static int getSavedHeight() {
        return Gdx.app.getPreferences(PREFS_NAME).getInteger("height", 1080);
    }

    public static float getMusicVolume() {
        return Gdx.app.getPreferences(PREFS_NAME).getFloat("musicVolume", 0.05f);
    }

    public static void saveMusicVolume(float volume) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putFloat("musicVolume", volume);
        prefs.flush();
    }

    public static boolean isMusicEnabled() {
        return Gdx.app.getPreferences(PREFS_NAME).getBoolean("musicEnabled", true);
    }

    public static void saveMusicEnabled(boolean enabled) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putBoolean("musicEnabled", enabled);
        prefs.flush();
    }
}