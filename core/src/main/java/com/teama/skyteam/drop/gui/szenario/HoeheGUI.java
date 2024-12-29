package com.teama.skyteam.drop.gui.szenario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.szenario.HoeheSzenarioTypes;

public class HoeheGUI {
    private Texture hoeheTexture;
    private float cardX, cardY;
    private final int MOVE_STEP = 96;
    private Spiel spiel;

    public HoeheGUI(Spiel spiel) {
        this.spiel = spiel;
        setHoeheTexture(spiel);
        cardX = 989;
        cardY = 841;
    }

    private void setHoeheTexture(Spiel spiel) {
        HoeheSzenarioTypes type = spiel.getSzenario().getHoeheSzenarioType();
        switch (type){
            case RED -> hoeheTexture = new Texture("images/hoehe/RED.png");
            case GREEN -> hoeheTexture = new Texture("images/hoehe/GREEN.png");
        }
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
