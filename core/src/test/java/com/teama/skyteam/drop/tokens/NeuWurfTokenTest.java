package com.teama.skyteam.drop.tokens;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spieler.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NeuWurfTokenTest {

    @Test
    void erneutWerfen() {
        Spiel s = new Spiel();
        Pilot p = new Pilot(s);

        ArrayList<Wuerfel> pWuerfels  = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pWuerfels.add(new Wuerfel());
        }
        p.setWuerfels(pWuerfels);

        ArrayList<Integer> oldWuerfelWerte = new ArrayList<>();
        for (Wuerfel w : pWuerfels) {
            oldWuerfelWerte.add(w.getWert());
        }
        NeuWurfToken t = new NeuWurfToken();
        p.neuWurfTokenBenutzen(t, p);

        ArrayList<Wuerfel> newWuerfels = p.getWuerfels();
        ArrayList<Integer> newWuerfelWerte = new ArrayList<>();
        for (Wuerfel w : newWuerfels) {
            newWuerfelWerte.add(w.getWert());
        }
        assertNotEquals(oldWuerfelWerte, newWuerfelWerte);

    }
}
