package com.teama.starpilots.drop.ui.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teama.starpilots.drop.MainBk;
import com.teama.starpilots.drop.ui.constants.TexturePaths;
import com.teama.starpilots.drop.ui.settings.SettingsManager;

public class StartScreen implements Screen {
    private final MainBk main;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Texture startScreenLogoTexture;

    public StartScreen(MainBk main) {
        this.main = main;
    }

    @Override
    public void show() {
        if (SettingsManager.isMusicEnabled() && !main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        viewport = new FitViewport(1920, 1080);
        batch = new SpriteBatch();
        startScreenLogoTexture = new Texture(TexturePaths.START_SCREEN_LOGO);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                main.setScreen(new MainMenuScreen(main));
            }
        }, 3.6f);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(startScreenLogoTexture, 0, 0, 1920, 1080);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        startScreenLogoTexture.dispose();
    }
}
