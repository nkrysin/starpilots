package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.tokens.Wuerfel;

public class Feld {

    private boolean belegt;
    private Wuerfel wuerfel;

    public Feld() {
        this.belegt = false;
        this.wuerfel = null;
    }


    public boolean isBelegt() {
        return belegt;
    }

    public Wuerfel getWuerfel() {
        return wuerfel;
    }

    public boolean wuerfelPlatzieren(Wuerfel w) {
        if (!belegt) {
            this.wuerfel = w;
            this.belegt = true;
            return true;
        }
        return false;
    }

    public void setWuerfel(Wuerfel w) {
        this.wuerfel = w;
        this.belegt = (w!=null);
    }

    public void clearOneUseFeld() {
        this.wuerfel = null;
        this.belegt = true;
    }
    public void setFree(){
        this.wuerfel = null;
        this.belegt = false;
    }

}
