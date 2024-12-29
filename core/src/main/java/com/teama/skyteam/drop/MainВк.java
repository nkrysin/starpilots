package com.teama.skyteam.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teama.skyteam.drop.gui.utilities.CameraKeyboardController;
import com.teama.skyteam.drop.gui.GameUIManager;

import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.spiel.Spiel;


import com.teama.skyteam.drop.spiel.SpielControllerGUI;
import com.teama.skyteam.drop.spiel.SpielSingletonConstructor;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainВк extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture tableTexture ;
    private Texture boardTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GameUIManager gameUIManager;
    private SpielControllerGUI spielControllerGUI;
    private Stage stage;
    private CameraKeyboardController cameraController;

    private final float tableWidth = 1920;
    private final float tableHeight = 1700;
    private Skin skin;

    @Override
    public void create() {
        //создание Spiel через фабрику
        Spiel spiel = SpielSingletonConstructor.createSpiel();

        //базовые объекты
        batch = new SpriteBatch();
        tableTexture = new Texture("images/spiel/main/table_w_arrows.png");
        boardTexture = new Texture("images/spiel/main/board.png");

        //GUI
        gameUIManager = new GameUIManager(spiel);

        //контроллер GUI для игрового процесса
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        stage = new Stage(viewport, batch);
        cameraController = new CameraKeyboardController(camera, tableHeight, stage);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("assets/skin/flat-earth-ui.json"));
        spielControllerGUI = new SpielControllerGUI(spiel, gameUIManager, stage, skin);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //стол
        batch.draw(tableTexture, 0, 0, tableWidth, tableHeight);

        //Здесь сценарии и высота
        gameUIManager.render(batch);

        //игровая доска
        batch.draw(boardTexture, 656, 115);

        batch.end();

        //элементы гуи на сцене
        spielControllerGUI.render();
        cameraController.updateAndRender(Gdx.graphics.getDeltaTime());
    }


    //Mir gefällt nicht, dass das Bild zusammengedrückt wird, wenn die Größe des Fensters geändert wird.
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        spielControllerGUI.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameUIManager.dispose();
        boardTexture.dispose();
        tableTexture.dispose();
        spielControllerGUI.dispose();
        TextureCache.dispose();
    }

}
