/**
 * Repräsentiert den Szenarioauswahl-Bildschirm im Spiel.
 *
 * Die Klasse `ScenarioSelectionScreen` ermöglicht es dem Spieler, ein Szenario auszuwählen,
 * mit dem das Spiel gestartet wird. Jedes Szenario wird durch eine Tastet dargestellt,
 * und es wird eine spezifische Route initialisiert. Der Bildschirm enthält außerdem eine Option,
 * zum Hauptmenü zurückzukehren.
 *
 * **Wichtig:** Beim Hinzufügen eines neuen Szenarios muss eine entsprechende Taste
 * mit einer Zuordnung zu diesem Szenario in diese Klasse aufgenommen werden.
 */
package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.gui.UILayout;
import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.model.game.SpielConstructor;
import com.teama.skyteam.drop.domain.model.szenario.Route;

class ScenarioSelectionScreen implements Screen {
    private final MainBk main;
    private Stage stage;
    private Skin skin;
    private Texture logo;
    private SpriteBatch batch;
    private Texture planeBackground;

    public ScenarioSelectionScreen(MainBk main) {
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

        TextButton montrealButton = new TextButton("Montreal Trudeau", blueButtonStyle);
        montrealButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithScenario(Route.MONTREAL_TRUDEAU);
            }
        });

        TextButton hanedaButton = new TextButton("Haneda", blueButtonStyle);
        hanedaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithScenario(Route.HANEDA);
            }
        });

        TextButton kualaButton = new TextButton("Kuala Lumpur", blueButtonStyle);
        kualaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithScenario(Route.KUALA_LUMPUR);
            }
        });

        TextButton goBackButton = new TextButton("Go back", blueButtonStyle);
        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new MainMenuScreen(main));
            }
        });

        float buttonWidth = 350f;
        table.add(montrealButton).width(buttonWidth).pad(20).row();
        table.add(hanedaButton).width(buttonWidth).pad(20).row();
        table.add(kualaButton).width(buttonWidth).pad(20).row();
        table.add(goBackButton).width(buttonWidth).pad(20).row();

        batch = new SpriteBatch();
        logo = new Texture(TexturePaths.LOGO_PATH);
        planeBackground = new Texture(TexturePaths.BACKGROUND_SZENARIO_SELECTION_PATH);
    }

    /**
     * Startet das Spiel mit dem ausgewählten Szenario.
     *
     * @param scenario Das Szenario, mit dem das Spiel initialisiert werden soll.
     */
    private void startGameWithScenario(Route scenario) {
        Spiel spiel = SpielConstructor.createSpiel(scenario);
        main.setScreen(new GameScreen(main, spiel));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(planeBackground, 0, 0, UILayout.SCREEN_SIZE[0], UILayout.SCREEN_SIZE[1]);
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
