package com.teama.skyteam.drop.gui.menu;

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
import com.teama.skyteam.drop.MainВк;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.gui.utilities.InputController;
import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.SpielControllerGUI;

public class GameScreen implements Screen {
    private final MainВк main;
    private final Spiel spiel;
    private final float tableWidth = 1920;
    private final float tableHeight = 1700;
    private SpriteBatch batch;
    private Texture tableTexture;
    private Texture boardTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SzenarioHoeheManager szenarioHoeheManager;
    private SpielControllerGUI spielControllerGUI;
    private Stage stage;
    private InputController inputController;
    private Skin skin;

    public GameScreen(MainВк main, Spiel spiel) {
        this.main = main;
        this.spiel = spiel;
    }

    @Override
    public void show() {
        if (main.menuMusic.isPlaying()) {
            main.menuMusic.stop();
        }

        batch = new SpriteBatch();

        tableTexture = TextureCache.get("images/spiel/main/table_w_arrows.png");
        boardTexture = TextureCache.get("images/spiel/main/board.png");

        szenarioHoeheManager = new SzenarioHoeheManager(spiel);

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));

        inputController = new InputController(camera, tableHeight, stage, main);
        Gdx.input.setInputProcessor(stage);

        spielControllerGUI = new SpielControllerGUI(spiel, szenarioHoeheManager, stage, skin);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tableTexture, 0, 0, tableWidth, tableHeight);
        szenarioHoeheManager.render(batch);
        batch.draw(boardTexture, 656, 115);
        batch.end();

        spielControllerGUI.render();
        inputController.updateAndRender(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spielControllerGUI.resize(width, height);
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

        if (szenarioHoeheManager != null) {
            szenarioHoeheManager.dispose();
            szenarioHoeheManager = null;
        }

        if (spielControllerGUI != null) {
            spielControllerGUI.dispose();
            spielControllerGUI = null;
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
