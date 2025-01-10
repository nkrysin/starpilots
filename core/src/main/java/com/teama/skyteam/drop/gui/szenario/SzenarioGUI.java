package com.teama.skyteam.drop.gui.szenario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.szenario.ReiseFeld;

import java.util.TreeMap;

public class SzenarioGUI {

    private final int MOVE_STEP = 96;
    private final float[][] planePositions = {
        {792, 886},
        {834, 886},
        {876, 886}
    };
    private Texture szenarioTexture;
    private Texture planeTexture;
    private Texture emptyTexture;
    private Spiel spiel;
    private String szenarioName;
    private int currentPosititon;
    private float cardX, cardY;

    private float offsetY = 0f;

    public SzenarioGUI(Spiel spiel) {
        this.spiel = spiel;
        planeTexture = new Texture("images/spiel/tokens/plane.png");
        emptyTexture = new Texture("images/spiel/main/empty.png");
        szenarioName = spiel.getSzenario().getSzenarioName();
        szenarioTexture = new Texture("images/szenario/" + szenarioName + ".png");
        currentPosititon = spiel.getCurrentPosition();

        cardX = 758;
        cardY = 845;
    }


    public void render(SpriteBatch batch) {
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        batch.draw(szenarioTexture, cardX, cardY - offsetY);

        int plane_y = 886;
        for (int i = currentPosititon; i < routeSzenario.size(); i++) {
            ReiseFeld currentField = routeSzenario.get(i);
            if (currentField != null) {
                int planesCount = currentField.getAnzahlFlugzeug();
                for (int j = 0; j < 3; j++) {
                    float x = planePositions[j][0];
                    float y = plane_y - offsetY;

                    if (j < planesCount) {
                        batch.draw(planeTexture, x, y, 39, 39);
                    } else {
                        batch.draw(emptyTexture, x, y, 39, 39);
                    }
                }
                plane_y += MOVE_STEP;
            }
        }
    }


    public void moveStep(int steps) {
        offsetY += steps * MOVE_STEP;
    }

    public void dispose() {
        szenarioTexture.dispose();
        planeTexture.dispose();
        emptyTexture.dispose();
    }
}
