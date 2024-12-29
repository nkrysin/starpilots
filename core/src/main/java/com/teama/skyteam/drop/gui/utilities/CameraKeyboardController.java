package com.teama.skyteam.drop.gui.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CameraKeyboardController {

    private OrthographicCamera camera;
    private float tableHeight;
    private Stage stage;

    private float cameraSpeed = 10f;

    private boolean limitCamera = true;

    public CameraKeyboardController(OrthographicCamera camera, float tableHeight, Stage stage){
        this.camera = camera;
        this.tableHeight = tableHeight;
        this.stage = stage;
        stage.setKeyboardFocus(stage.getRoot());
    }

    public void updateAndRender(float delta){
        handleKeyboard();
        stage.act(delta);
        stage.draw();
    }

    private void handleKeyboard(){
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
