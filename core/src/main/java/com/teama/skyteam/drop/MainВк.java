package com.teama.skyteam.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teama.skyteam.drop.gui.DragProcessor;
import com.teama.skyteam.drop.gui.GameUIManager;

import com.teama.skyteam.drop.gui.HoeheGUI;
import com.teama.skyteam.drop.gui.SzenarioGUI;
import com.teama.skyteam.drop.spiel.Spiel;


import com.teama.skyteam.drop.spiel.SpielControllerGUI;
import com.teama.skyteam.drop.spiel.SpielFactory;
import com.teama.skyteam.drop.spiel.SpielLogik;

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
    private SpielLogik spielLogik;


    private final float windowHeight = 1080;

    private final float tableWidth = 1920;
    private final float tableHeight = 1700;

    @Override
    public void create() {
        Spiel spiel = SpielFactory.createSpiel();
        spielLogik = new SpielLogik(spiel);
        gameUIManager = new GameUIManager();


        batch = new SpriteBatch();
        tableTexture  = new Texture("images/spiel/main/table.png");
        boardTexture = new Texture("images/spiel/main/board.png");

        camera = new OrthographicCamera();
        viewport = new FitViewport(tableWidth, windowHeight, camera);
        viewport.apply();
        camera.position.set(tableWidth / 2f, windowHeight / 2f, 0);
        camera.update();

        gameUIManager = new GameUIManager();
        szenarioGUI = new SzenarioGUI();
        hoeheGUI = new HoeheGUI();

        Gdx.input.setInputProcessor(new DragProcessor(camera, tableHeight));

        spielControllerGUI = new SpielControllerGUI(spiel, spielLogik, gameUIManager);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tableTexture, 0, 0, tableWidth, tableHeight);

        //Здесь сценарии и высоты
        szenarioGUI.render(batch);
        hoeheGUI.render(batch);


        batch.draw(boardTexture, 656, 115);


        //Здесь отрисовываем кубики и прочее

        spielControllerGUI.spielenSchritt();

        batch.end();
    }


    //Mir gefällt nicht, dass das Bild zusammengedrückt wird, wenn die Größe des Fensters geändert wird.
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        szenarioGUI.dispose();
        hoeheGUI.dispose();
        boardTexture.dispose();
        tableTexture.dispose();
    }
}
