/**
 * Repräsentiert den Hauptbildschirm des Spiels, auf dem das Gameplay stattfindet.
 *
 * Die Klasse `GameScreen` kümmert sich um das Rendern und die Verwaltung der Spieloberfläche,
 * einschließlich der Spielfelder, Interaktionen und Kameraansicht.
 * Sie integriert verschiedene Controller und Manager, um das Spielerlebnis zu steuern.
 */
package com.teama.skyteam.drop.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.ui.view.CampaignAltitudeView;
import com.teama.skyteam.drop.ui.GameControllerGUI;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.constants.UILayout;
import com.teama.skyteam.drop.ui.utilities.InputController;
import com.teama.skyteam.drop.ui.utilities.TextureCache;

public class GameScreen implements Screen {
    private final MainBk main;
    private final Game game;
    private SpriteBatch batch;
    private Texture tableTexture;
    private Texture boardTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private CampaignAltitudeView campaignAltitudeView;
    private GameControllerGUI gameControllerGUI;
    private Stage stage;
    private InputController inputController;
    private Skin skin;

    public GameScreen(MainBk main, Game game) {
        this.main = main;
        this.game = game;
    }

    @Override
    public void show() {
        if (main.menuMusic.isPlaying()) {
            main.menuMusic.stop();
        }

        batch = new SpriteBatch();

        tableTexture = TextureCache.get(TexturePaths.TABLE_PATH);
        boardTexture = TextureCache.get(TexturePaths.BOARD_PATH);

        campaignAltitudeView = new CampaignAltitudeView(game);

        camera = new OrthographicCamera();
        viewport = new FitViewport(UILayout.SCREEN_SIZE[0], UILayout.SCREEN_SIZE[1], camera);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal(TexturePaths.SKIN_PATH));

        inputController = new InputController(camera, UILayout.TABLE_SIZE[1], stage, main);
        Gdx.input.setInputProcessor(stage);

        gameControllerGUI = new GameControllerGUI(game, campaignAltitudeView, stage, skin);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tableTexture, 0, 0, UILayout.TABLE_SIZE[0], UILayout.TABLE_SIZE[1]);
        campaignAltitudeView.render(batch);
        batch.draw(boardTexture, 656, 115);
        batch.end();

        gameControllerGUI.render();
        inputController.updateAndRender(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        gameControllerGUI.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        if (campaignAltitudeView != null) {
            campaignAltitudeView.dispose();
            campaignAltitudeView = null;
        }

        if (gameControllerGUI != null) {
            gameControllerGUI.dispose();
            gameControllerGUI = null;
        }

        if (skin != null) {
            skin.dispose();
            skin = null;
        }

        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }
}
