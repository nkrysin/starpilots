package com.teama.skyteam.drop.gui.utilities;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class SelectedWuerfelData {
    private final Spieler spieler;
    private final int wuerfelIndex;
    private final Wuerfel wuerfel;

    public SelectedWuerfelData(Spieler spieler, int wuerfelIndex, Wuerfel wuerfel) {
        this.spieler = spieler;
        this.wuerfelIndex = wuerfelIndex;
        this.wuerfel = wuerfel;
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
}
