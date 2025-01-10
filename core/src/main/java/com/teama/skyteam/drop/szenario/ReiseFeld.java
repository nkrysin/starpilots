package com.teama.skyteam.drop.szenario;

public class ReiseFeld {
    private final boolean istFlughafen;
    private int planes;

    ReiseFeld(int planes, boolean istFlughafen) {

        this.planes = planes;
        this.istFlughafen = istFlughafen;
    }


    public void removeFlugzeug() {
        setAnzahlFlugzeug(getAnzahlFlugzeug() - 1);
    }

    public int getAnzahlFlugzeug() {
        return planes;
    }

    public void setAnzahlFlugzeug(int planes) {
        this.planes = planes;
    }

    public boolean istFlughafen() {
        return istFlughafen;
    }
}


