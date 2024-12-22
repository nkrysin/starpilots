package com.teama.skyteam.drop.gui.szenario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HoeheGUI {
    private Texture hoeheTexture;
    private float cardX, cardY;
    private final int MOVE_STEP = 96;

    public HoeheGUI() {

        //TODO реализовать установку карточки в зависимости от сценария высота и предусмотреть, что GREEN = GREEN || ORANGE; RED = RED || BLACK
        hoeheTexture = new Texture("images/hoehe/GREEN.png");

        cardX = 989;
        cardY = 841;
    }

    public void render(SpriteBatch batch) {
        batch.draw(hoeheTexture, cardX, cardY);
    }

    public void dispose() {
        hoeheTexture.dispose();
    }


    public void moveStep() {
        cardY -= MOVE_STEP;
    }




}
