package com.teama.skyteam.drop.tokens;

import com.teama.skyteam.drop.spieler.Spieler;

import java.util.ArrayList;

public class NeuWurfToken {

    public ArrayList<Wuerfel> erneutWerfen(Spieler spieler){
        int anzahlWuerfel = spieler.getAnzahlWuerfels();
        ArrayList<Wuerfel> neuWurfe = new ArrayList<Wuerfel>();
        for(int i = 0; i < anzahlWuerfel; i++){
            neuWurfe.add(new Wuerfel());
        }
        return neuWurfe;
    }


}
