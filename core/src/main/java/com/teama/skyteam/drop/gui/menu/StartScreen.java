/**
 * Repräsentiert den Startbildschirm des Spiels.
 *
 * Die Klasse `StartScreen` zeigt nacheinander zwei Bilder an:
 * 1. Ein Team-A-Logo.
 * 2. Ein Startbildschirm-Logo.
 *
 * Nach einer festgelegten Zeit wechselt der Bildschirm automatisch zum Hauptmenü.
 * Der Startbildschirm dient als Einführung, bevor der Player in das Menü gelangt.
 */
package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.gui.UILayout;

public class StartScreen implements Screen {
    private final MainBk main;
    private SpriteBatch batch;
    private Texture teamATexture;
    private Texture startScreenLogoTexture;
    private boolean showSecondImage = false;

    public StartScreen(MainBk main) {
        this.main = main;
    }

    @Override
    public void show() {
        if (!main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        batch = new SpriteBatch();
        teamATexture = new Texture(TexturePaths.TEAM_A_LOGO);
        startScreenLogoTexture = new Texture(TexturePaths.START_SCREEN_LOGO);

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
        }, 6.6f);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        if (!showSecondImage) {
            batch.draw(teamATexture, 0, 0, UILayout.SCREEN_SIZE[0], UILayout.SCREEN_SIZE[1]);
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
