/**
 * Repräsentiert die GUI-Komponente für die Höhenanzeige im Spiel.
 *
 * Die Klasse `HoeheGUI` zeigt die aktuelle Höhenkarte basierend auf dem Szenariotyp an
 * und bietet die Möglichkeit, die Karte schrittweise zu verschieben (z. B. nach unten).
 * Sie nutzt die Höhe des Szenarios aus dem Spiel, um die entsprechende Karte auszuwählen.
 */
package com.teama.skyteam.drop.gui.szenario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.gui.UILayout;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.szenario.HoeheSzenarioTypes;

public class HoeheGUI {
    private Texture hoeheTexture;
    private float cardX, cardY;
    private Spiel spiel;

    /**
     * Konstruktor zur Initialisierung der Höhen-GUI.
     *
     * @param spiel Das aktuelle Spielobjekt, das die Szenarioinformationen bereitstellt.
     * cardX = 989 und cardY = 841 gelten nur für FUllHD-Bildschirme
     */
    public HoeheGUI(Spiel spiel) {
        this.spiel = spiel;
        setHoeheTexture(spiel);
        cardX = UILayout.HOEHE_FULL_HD_X_SIZE;
        cardY = UILayout.HOEHE_FULL_HD_Y_SIZE;
    }

    /**
     * Wählt die Textur der Höhenkarte basierend auf dem Szenariotyp.
     *
     * @param spiel Das aktuelle Spielobjekt, das den Typ des Höhen-Szenarios enthält.
     */
    private void setHoeheTexture(Spiel spiel) {
        HoeheSzenarioTypes type = spiel.getSzenario().getHoeheSzenarioType();
        switch (type) {
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

    /**
     * Bewegt die Höhenkarte um einen festen Schritt nach unten.
     */
    public void moveStep() {
        cardY -= UILayout.MOVE_STEP;
    }
}
