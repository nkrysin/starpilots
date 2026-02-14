/**
 * Verwalter für das Campaign- und Höhenmanagement in der Game-GUI.
 *
 * Die Klasse `CampaignAltitudeManager` koordiniert die Aktualisierung und Darstellung der
 * grafischen Campaign- und Höhenkomponenten basierend auf der aktuellen Spielzustand.
 */
package com.teama.skyteam.drop.gui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.gui.campaign.AltitudeGUI;
import com.teama.skyteam.drop.gui.campaign.CampaignGUI;

public class CampaignAltitudeManager {
    private CampaignGUI campaignGUI;
    private AltitudeGUI altitudeGUI;

    /**
     * Konstruktor zur Initialisierung der GUI-Komponenten für Campaign und Höhe.
     *
     * @param game Die aktuelle Game-Instanz, aus der die Szenarien und Höheninformationen abgerufen werden.
     */
    public CampaignAltitudeManager(Game game) {
        this.campaignGUI = new CampaignGUI(game);
        this.altitudeGUI = new AltitudeGUI(game);
    }

    /**
     * Aktualisiert das Campaign basierend auf der aktuellen Geschwindigkeit.
     *
     * @param currentSpeed Die aktuelle Geschwindigkeit des Spiels.
     */
    public void updateCampaign(int currentSpeed) {
        campaignGUI.moveStep(currentSpeed);
    }

    /**
     * Aktualisiert die Höhenanzeige.
     */
    public void updateAltitude() {
        altitudeGUI.moveStep();
    }

    public void render(SpriteBatch batch) {
        campaignGUI.render(batch);
        altitudeGUI.render(batch);
    }

    public void dispose() {
        campaignGUI.dispose();
        altitudeGUI.dispose();
    }
}
