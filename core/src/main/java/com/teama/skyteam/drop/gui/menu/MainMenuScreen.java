/**
 * Repräsentiert den Hauptmenübildschirm des Spiels.
 *
 * Die Klasse `MainMenuScreen` zeigt das Hauptmenü des Spiels,
 * in dem der Spieler Optionen wie "Spiel starten", "Credits" und "Beenden" auswählen kann.
 * Der Bildschirm enthält einen Hintergrund, ein Logo und interaktive Buttons.
 */
package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.skyteam.drop.MainBk;

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
        if (!main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        stage = new Stage();
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
        table.add(creditsButton).width(buttonWidth).pad(20).row();
        table.add(exitButton).width(buttonWidth).pad(20).row();

        batch = new SpriteBatch();
        logo = new Texture("images/spiel/menu/logo.png");
        planeBackground = new Texture("images/spiel/menu/bg_menu1.png");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(planeBackground, 0, 0, 1920, 1080);
        batch.draw(logo, 519, 720, 883, 220);
        batch.end();

        stage.act(delta);
        stage.draw();
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
    }
}
