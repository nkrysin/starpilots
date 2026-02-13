/**
 * Repräsentiert die Daten eines ausgewählten Würfels im Spiel.
 *
 * Die Klasse `SelectedWuerfelData` speichert Informationen über den Spieler, der den Würfel ausgewählt hat,
 * den Index des Würfels und die zugehörige Würfelinstanz. Außerdem wird der Pfad zur Textur des Würfels
 * basierend auf dem Würfelwert und dem Spielertyp (Pilot oder Copilot) generiert.
 */
package com.teama.skyteam.drop.gui.utilities;

import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.spieler.Spieler;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class SelectedWuerfelData {

    private final Spieler spieler;
    private final int wuerfelIndex;
    private final Wuerfel wuerfel;
    private final String diceTexturePath;

    /**
     * Konstruktor zur Initialisierung der Daten des ausgewählten Würfels.
     *
     * @param spieler der Spieler, der den Würfel ausgewählt hat.
     * @param wuerfelIndex der Index des ausgewählten Würfels.
     * @param wuerfel die Instanz des ausgewählten Würfels.
     */
    public SelectedWuerfelData(Spieler spieler, int wuerfelIndex, Wuerfel wuerfel) {
        this.spieler = spieler;
        this.wuerfelIndex = wuerfelIndex;
        this.wuerfel = wuerfel;

        int wert = wuerfel.getWert();
        boolean isPilot = spieler instanceof Pilot;
        if (isPilot) {
            this.diceTexturePath = TexturePaths.WUERFEL_PATH_PILOT + wert + TexturePaths.IMG_FORMAT;
        } else {
            this.diceTexturePath = TexturePaths.WUERFEL_PATH_COPILOT + wert + TexturePaths.IMG_FORMAT;
        }
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public int getWuerfelIndex() {
        return wuerfelIndex;
    }

    public Wuerfel getWuerfel() {
        return wuerfel;
    }


    public String getDiceTexturePath() {
        return diceTexturePath;
    }
}
