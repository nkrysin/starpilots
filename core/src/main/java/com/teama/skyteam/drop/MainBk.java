package com.teama.skyteam.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.teama.skyteam.drop.gui.menu.StartScreen;
import com.teama.skyteam.drop.gui.utilities.TextureCache;

public class MainBk extends ApplicationAdapter {

    public Music menuMusic;
    private Screen currentScreen;

    // Diese Methode setzt einen neuen Bildschirm (Screen) und verwaltet den Wechsel
    // zwischen den Bildschirmen. Der aktuelle Bildschirm wird versteckt und entsorgt.
    public void setScreen(Screen newScreen) {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }
        currentScreen = newScreen;
        if (currentScreen != null) {
            currentScreen.show();
        }
    }

    // Diese Methode wird beim Start der Anwendung aufgerufen.
    // Initialisiert die Hintergrundmusik des Hauptmenüs.
    @Override
    public void create() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/main_menu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.05f);
        setScreen(new StartScreen(this));
    }

    // Diese Methode wird kontinuierlich aufgerufen, um den Bildschirm zu aktualisieren.
    @Override
    public void render() {
        if (currentScreen == null) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    // Diese Methode wird aufgerufen, wenn das Fenster oder der Bildschirm die Größe ändert.
    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    // Diese Methode wird aufgerufen, wenn die Anwendung pausiert wird (z. B. bei Minimierung)
    @Override
    public void pause() {
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    // Diese Methode wird aufgerufen, wenn die Anwendung fortgesetzt wird.
    @Override
    public void resume() {
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    // Diese Methode wird aufgerufen, wenn die Anwendung beendet wird.
    @Override
    public void dispose() {
        super.dispose();
        TextureCache.dispose();
        menuMusic.dispose();
    }
}
