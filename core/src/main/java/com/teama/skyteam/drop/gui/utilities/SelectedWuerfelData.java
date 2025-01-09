package com.teama.skyteam.drop.gui.utilities;

import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class SelectedWuerfelData {

    private final Spieler spieler;
    private final int wuerfelIndex;
    private final Wuerfel wuerfel;
    private final String diceTexturePath;

    public SelectedWuerfelData(Spieler spieler, int wuerfelIndex, Wuerfel wuerfel) {
        this.spieler = spieler;
        this.wuerfelIndex = wuerfelIndex;
        this.wuerfel = wuerfel;

        int wert = wuerfel.getWert();
        boolean isPilot = spieler instanceof Pilot;
        if (isPilot) {
            this.diceTexturePath = "images/spiel/dices/blue/blue_" + wert + ".png";
        } else {
            this.diceTexturePath = "images/spiel/dices/orange/orange_" + wert + ".png";
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
