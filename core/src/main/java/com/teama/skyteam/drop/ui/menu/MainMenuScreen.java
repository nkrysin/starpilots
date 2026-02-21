/**
 * Repräsentiert den Hauptmenübildschirm des Spiels.
 *
 * Die Klasse `MainMenuScreen` zeigt das Hauptmenü des Spiels,
 * in dem der Player Optionen wie "Game starten", "Credits" und "Beenden" auswählen kann.
 * Der Bildschirm enthält einen Hintergrund, ein Logo und interaktive Buttons.
 */
package com.teama.skyteam.drop.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.settings.SettingsManager;

public class MainMenuScreen implements Screen {
    private final MainBk main;
    private Stage stage;
    private Texture logo;
    private SpriteBatch batch;
    private Texture planeBackground;

    public MainMenuScreen(MainBk main) {
        this.main = main;
    }

    @Override
    public void show() {
        if (SettingsManager.isMusicEnabled() && !main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        FitViewport viewport = new FitViewport(1920, 1080);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle blueButtonStyle = ButtonStyle.style();

        TextButton startButton = new TextButton("Start Game", blueButtonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new ScenarioSelectionScreen(main));
            }
        });

        TextButton settingsButton = new TextButton("Settings", blueButtonStyle);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new SettingsScreen(main));
            }
        });

        TextButton creditsButton = new TextButton("Credits", blueButtonStyle);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new CreditsScreen(main));
            }
        });

        TextButton exitButton = new TextButton("Exit", blueButtonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        float buttonWidth = 350f;
        table.add(startButton).width(buttonWidth).pad(20).row();
        table.add(settingsButton).width(buttonWidth).pad(20).row();
        table.add(creditsButton).width(buttonWidth).pad(20).row();
        table.add(exitButton).width(buttonWidth).pad(20).row();

        batch = new SpriteBatch();
        logo = new Texture(TexturePaths.LOGO_PATH);
        planeBackground = new Texture(TexturePaths.BACKGROUND_MENU_PATH);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        batch.draw(planeBackground, 0, 0, 1920, 1080);
        batch.draw(logo, 519, 720, 883, 220);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }
}
