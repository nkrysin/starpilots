package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class DragProcessor extends InputAdapter {
    private float lastY;
    private OrthographicCamera camera;
    private float tableHeight;

    public DragProcessor(OrthographicCamera camera, float tableHeight) {
        this.camera = camera;

        this.tableHeight = tableHeight;

    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        lastY = screenY;
        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        float deltaY = (screenY - lastY);
        lastY = screenY;


        //Camera bewegt sich nur neben oY
        camera.translate(0, deltaY);

        float halfViewHeight = camera.viewportHeight * camera.zoom / 2f;

        if (camera.position.y < halfViewHeight) {
            camera.position.y = halfViewHeight;
        }

        if (camera.position.y > tableHeight - halfViewHeight) {
            camera.position.y = tableHeight - halfViewHeight;
        }

        camera.update();
        return true;
    }
}
