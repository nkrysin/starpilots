package com.teama.skyteam.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teama.skyteam.drop.gui.utilities.DragProcessor;
import com.teama.skyteam.drop.gui.GameUIManager;

import com.teama.skyteam.drop.gui.szenario.HoeheGUI;
import com.teama.skyteam.drop.gui.szenario.SzenarioGUI;
import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.spiel.Spiel;


import com.teama.skyteam.drop.spiel.SpielControllerGUI;
import com.teama.skyteam.drop.spiel.SpielFactory;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainВк extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture tableTexture ;
    private Texture boardTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SzenarioGUI szenarioGUI;
    private HoeheGUI hoeheGUI;
    private GameUIManager gameUIManager;
    private SpielControllerGUI spielControllerGUI;



    private final float windowHeight = 1080;

    private final float tableWidth = 1920;
    private final float tableHeight = 1700;
    private Skin skin;

    @Override
    public void create() {
        //создание Spiel через фабрику
        Spiel spiel = SpielFactory.createSpiel();

        //базовые объекты
        batch = new SpriteBatch();
        tableTexture = new Texture("images/spiel/main/table.png");
        boardTexture = new Texture("images/spiel/main/board.png");

        //камера
        camera = new OrthographicCamera();
        viewport = new FitViewport(tableWidth, windowHeight, camera);
        viewport.apply();
        camera.position.set(tableWidth / 2f, windowHeight / 2f, 0);
        camera.update();

        //GUI
        gameUIManager = new GameUIManager();
        szenarioGUI = new SzenarioGUI();
        hoeheGUI = new HoeheGUI();


        //контроллер GUI для игрового процесса
        Stage stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("assets/skin/flat-earth-ui.json"));
        spielControllerGUI = new SpielControllerGUI(spiel, gameUIManager, stage, skin);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new DragProcessor(camera, tableHeight));
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //стол
        batch.draw(tableTexture, 0, 0, tableWidth, tableHeight);

        //Здесь сценарии и высота
        szenarioGUI.render(batch);
        hoeheGUI.render(batch);

        //игровая доска
        batch.draw(boardTexture, 656, 115);

        batch.end();

        //элементы гуи на сцене
        spielControllerGUI.render();
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
        szenarioGUI.dispose();
        hoeheGUI.dispose();
        boardTexture.dispose();
        tableTexture.dispose();
        spielControllerGUI.dispose();
        TextureCache.dispose();
    }

}
