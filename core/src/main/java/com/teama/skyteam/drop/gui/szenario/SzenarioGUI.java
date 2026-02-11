/**
 * Repräsentiert die GUI-Komponente für das Szenario im Spiel.
 *
 * Die Klasse `SzenarioGUI` rendert die aktuelle Route des Szenarios und die Flugzeugpositionen
 * basierend auf dem aktuellen Spielzustand. Sie ermöglicht auch die Bewegung der Ansicht
 * durch Schritte entlang der Route.
 */
package com.teama.skyteam.drop.gui.szenario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.gui.UILayout;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.szenario.ReiseFeld;

import java.util.TreeMap;

public class SzenarioGUI {

    private Texture szenarioTexture;
    private Texture planeTexture;
    private Texture emptyTexture;
    private Spiel spiel;
    private String szenarioName;
    private int currentPosititon;
    private float cardX, cardY;
    private float offsetY = UILayout.SZENARIO_PLANE_Y_OFFSET;

    public SzenarioGUI(Spiel spiel) {
        this.spiel = spiel;
        planeTexture = new Texture(TexturePaths.PLANE_PATH);
        emptyTexture = new Texture(TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        szenarioName = spiel.getSzenario().getSzenarioName();
        szenarioTexture = new Texture(TexturePaths.ASSETS_SZENARIO_PATH + szenarioName + TexturePaths.IMG_FORMAT);
        currentPosititon = spiel.getCurrentPosition();

        cardX = UILayout.SZENARIO_COORDINATES[0];
        cardY = UILayout.SZENARIO_COORDINATES[1];
    }


    public void render(SpriteBatch batch) {
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        batch.draw(szenarioTexture, cardX, cardY - offsetY);

        int plane_y = UILayout.SZENARIO_PLANE_Y_COORDINATE;
        for (int i = currentPosititon; i < routeSzenario.size(); i++) {
            ReiseFeld currentField = routeSzenario.get(i);
            if (currentField != null) {
                int planesCount = currentField.getAnzahlFlugzeug();
                for (int j = 0; j < 3; j++) {
                    float x = UILayout.PLANE_POSITIONS[j][0];
                    float y = plane_y - offsetY;

                    if (j < planesCount) {
                        batch.draw(planeTexture, x, y, UILayout.SZENARIO_PLANE_SIZE[0], UILayout.SZENARIO_PLANE_SIZE[1]);
                    } else {
                        batch.draw(emptyTexture, x, y, UILayout.SZENARIO_PLANE_SIZE[0], UILayout.SZENARIO_PLANE_SIZE[1]);
                    }
                }
                plane_y += UILayout.MOVE_STEP;
            }
        }
    }

    /**
     * Bewegt die Karte um eine bestimmte Anzahl von Schritten.
     *
     * @param steps Die Anzahl der Schritte, um die die Karte bewegt werden soll.
     */
    public void moveStep(int steps) {
        offsetY += steps * UILayout.MOVE_STEP;
    }

    public void dispose() {
        szenarioTexture.dispose();
        planeTexture.dispose();
        emptyTexture.dispose();
    }
}
