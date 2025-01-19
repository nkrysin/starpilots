/**
 * Verwalter für das Szenario- und Höhenmanagement in der Spiel-GUI.
 *
 * Die Klasse `SzenarioHoeheManager` koordiniert die Aktualisierung und Darstellung der
 * grafischen Szenario- und Höhenkomponenten basierend auf der aktuellen Spielzustand.
 */
package com.teama.skyteam.drop.gui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.gui.szenario.HoeheGUI;
import com.teama.skyteam.drop.gui.szenario.SzenarioGUI;
import com.teama.skyteam.drop.spiel.Spiel;

public class SzenarioHoeheManager {
    private SzenarioGUI szenarioGUI;
    private HoeheGUI hoeheGUI;

    /**
     * Konstruktor zur Initialisierung der GUI-Komponenten für Szenario und Höhe.
     *
     * @param spiel Die aktuelle Spiel-Instanz, aus der die Szenarien und Höheninformationen abgerufen werden.
     */
    public SzenarioHoeheManager(Spiel spiel) {
        this.szenarioGUI = new SzenarioGUI(spiel);
        this.hoeheGUI = new HoeheGUI(spiel);
    }

    /**
     * Aktualisiert das Szenario basierend auf der aktuellen Geschwindigkeit.
     *
     * @param currentSpeed Die aktuelle Geschwindigkeit des Spiels.
     */
    public void updateScenario(int currentSpeed) {
        szenarioGUI.moveStep(currentSpeed);
    }

    /**
     * Aktualisiert die Höhenanzeige.
     */
    public void updateHoehe() {
        hoeheGUI.moveStep();
    }

    public void render(SpriteBatch batch) {
        szenarioGUI.render(batch);
        hoeheGUI.render(batch);
    }

    public void dispose() {
        szenarioGUI.dispose();
        hoeheGUI.dispose();
    }
}
