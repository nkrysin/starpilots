package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SzenarioGUI {
    private Texture szenarioTexture;
    private float cardX, cardY;
    private final int MOVE_STEP = 96;

    public SzenarioGUI() {


        szenarioTexture = new Texture("images/szenario/Haneda.png");

        cardX = 758;
        cardY = 845;
    }

    public void render(SpriteBatch batch) {
        batch.draw(szenarioTexture, cardX, cardY);
    }

    public void dispose() {
        szenarioTexture.dispose();
    }


    public void moveStep(int currentSpeed) {
        cardY -= currentSpeed * MOVE_STEP;
    }




}
