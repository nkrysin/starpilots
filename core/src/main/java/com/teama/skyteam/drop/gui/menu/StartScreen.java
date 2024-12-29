package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.teama.skyteam.drop.MainВк;

public class StartScreen implements Screen {
    private final MainВк main;
    private SpriteBatch batch;
    private Texture teamATexture;
    private Texture startScreenLogoTexture;
    private boolean showSecondImage = false;

    public StartScreen(MainВк main) {
        this.main = main;
    }

    @Override
    public void show() {
        if (!main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        batch = new SpriteBatch();
        teamATexture = new Texture("images/spiel/menu/team_a.png");
        startScreenLogoTexture = new Texture("images/spiel/menu/start_screen_logo.png");

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                showSecondImage = true;
            }
        }, 3f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                main.setScreen(new MainMenuScreen(main));
            }
        }, 6f);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        if (!showSecondImage) {
            batch.draw(teamATexture, 0, 0, 1920,1080);
        } else {
            batch.draw(startScreenLogoTexture, 0, 0);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
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
        teamATexture.dispose();
        startScreenLogoTexture.dispose();
    }
}
