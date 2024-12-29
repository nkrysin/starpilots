package com.teama.skyteam.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.teama.skyteam.drop.gui.menu.StartScreen;
import com.badlogic.gdx.audio.Music;

public class MainВк extends ApplicationAdapter {

    private Screen currentScreen;
    public Music menuMusic;

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

    @Override
    public void create() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/main_menu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.35f);
        setScreen(new StartScreen(this));
    }

    @Override
    public void render() {
        if (currentScreen == null) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        currentScreen.resize(width, height);
    }

    @Override
    public void pause() {
        currentScreen.pause();
    }

    @Override
    public void resume() {
        currentScreen.resume();
    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        currentScreen.dispose();
    }
}
