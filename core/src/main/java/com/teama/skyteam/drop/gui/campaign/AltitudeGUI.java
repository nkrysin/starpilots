/**
 * Repräsentiert die GUI-Komponente für die Höhenanzeige im Game.
 *
 * Die Klasse `AltitudeGUI` zeigt die aktuelle Höhenkarte basierend auf dem Szenariotyp an
 * und bietet die Möglichkeit, die Karte schrittweise zu verschieben (z. B. nach unten).
 * Sie nutzt die Höhe des Szenarios aus dem Game, um die entsprechende Karte auszuwählen.
 */
package com.teama.skyteam.drop.gui.campaign;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.gui.UILayout;
import com.teama.skyteam.drop.domain.model.campaign.AltitudeCampaignTypes;

public class AltitudeGUI {
    private Texture hoeheTexture;
    private float cardX, cardY;
    private Game game;

    /**
     * Konstruktor zur Initialisierung der Höhen-GUI.
     *
     * @param game Das aktuelle Spielobjekt, das die Szenarioinformationen bereitstellt.
     * cardX = 989 und cardY = 841 gelten nur für FUllHD-Bildschirme
     */
    public AltitudeGUI(Game game) {
        this.game = game;
        setHoeheTexture(game);
        cardX = UILayout.HOEHE_FULL_HD_X_SIZE;
        cardY = UILayout.HOEHE_FULL_HD_Y_SIZE;
    }

    /**
     * Wählt die Textur der Höhenkarte basierend auf dem Szenariotyp.
     *
     * @param game Das aktuelle Spielobjekt, das den Typ des Höhen-Szenarios enthält.
     */
    private void setHoeheTexture(Game game) {
        AltitudeCampaignTypes type = game.getSzenario().getHoeheSzenarioType();
        switch (type) {
            case RED -> hoeheTexture = new Texture(TexturePaths.HOEHE_RED_PATH);
            case GREEN -> hoeheTexture = new Texture(TexturePaths.HOEHE_GREEN_PATH);
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
