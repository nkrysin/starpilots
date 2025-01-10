package com.teama.skyteam.drop.gui.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.gui.menu.ButtonStyle;
import com.teama.skyteam.drop.gui.menu.MainMenuScreen;

public class InputController {

    private OrthographicCamera camera;
    private float tableHeight;
    private Stage stage;
    private Stage pauseStage;

    private float cameraSpeed = 10f;

    private boolean limitCamera = true;
    private boolean isPaused = false;

    private MainBk main;

    private Image pauseBackground;

    public InputController(OrthographicCamera camera, float tableHeight, Stage stage, MainBk main){
        this.camera = camera;
        this.tableHeight = tableHeight;
        this.stage = stage;
        this.main = main;
        stage.setKeyboardFocus(stage.getRoot());

        createPauseStage();
    }

    private void createPauseStage(){
        pauseStage = new Stage(new ScreenViewport());

        Texture backgroundTexture = new Texture("images/spiel/main/pause_background.png");
        pauseBackground = new Image(backgroundTexture);
        pauseBackground.setFillParent(true);
        pauseStage.addActor(pauseBackground);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton.TextButtonStyle blueButtonStyle = ButtonStyle.style();

        TextButton resumeButton = new TextButton("Resume", blueButtonStyle);
        TextButton returnButton = new TextButton("Return to menu", blueButtonStyle);
        TextButton exitButton = new TextButton("Exit", blueButtonStyle);

        resumeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                resumeGame();
            }
        });

        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                returnToMenu();
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                exitGame();
            }
        });

        float buttonWidth = 350f;
        table.add(resumeButton).width(buttonWidth).pad(20).row();
        table.add(returnButton).width(buttonWidth).pad(20).row();
        table.add(exitButton).width(buttonWidth).pad(20).row();

        pauseStage.addActor(table);
    }

    public void updateAndRender(float delta){
        if(!isPaused){
            handleKeyboard();
            stage.act(delta);
            stage.draw();
        } else {
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }

    private void handleKeyboard(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        if (!isPaused) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.position.y += cameraSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.position.y -= cameraSpeed;
            }

            if (limitCamera) {
                float halfViewHeight = camera.viewportHeight * camera.zoom / 2f;
                if (camera.position.y < halfViewHeight) {
                    camera.position.y = halfViewHeight;
                }
                if (camera.position.y > tableHeight - halfViewHeight) {
                    camera.position.y = tableHeight - halfViewHeight;
                }
            }
            camera.update();
        }
    }

    private void togglePause(){
        isPaused = !isPaused;
        if(isPaused){
            Gdx.input.setInputProcessor(pauseStage);
        } else {
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void resumeGame(){
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
    }

    private void returnToMenu(){
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
        main.setScreen(new MainMenuScreen(main));
    }

    private void exitGame(){
        Gdx.app.exit();
    }

    public void dispose(){
        if (pauseBackground.getDrawable() instanceof TextureRegionDrawable) {
            Texture texture = ((TextureRegionDrawable) pauseBackground.getDrawable()).getRegion().getTexture();
            texture.dispose();
        }
        pauseStage.dispose();
    }
}
