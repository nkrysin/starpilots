package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.tokens.Wuerfel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeldTest {

    private Feld feld;

    @BeforeEach
    void setUp() {
        feld = new Feld();
    }

    @Test
    void testInitialState() {
        assertFalse(feld.isBelegt(), "Feld sollte leer sein");
        assertNull(feld.getWuerfel(), "Wuerfel sollte null sein");
    }

    @Test
    void testWuerfelPlatzierenSuccess() {
        Wuerfel wuerfel = new Wuerfel();
        boolean result = feld.wuerfelPlatzieren(wuerfel);

        assertTrue(result);
        assertTrue(feld.isBelegt());
        assertEquals(wuerfel, feld.getWuerfel());
    }

    @Test
    void testClearOneUseFeld() {
        Wuerfel wuerfel = new Wuerfel();
        feld.setWuerfel(wuerfel);

        feld.clearOneUseFeld();

        assertTrue(feld.isBelegt());
        assertNull(feld.getWuerfel());
    }

    @Test
    void testSetWuerfelSetToNull() {
        feld.setWuerfel(null);

        assertFalse(feld.isBelegt());
        assertNull(feld.getWuerfel());
    }


    @Test
    void testSetWuerfelSetToNonNull() {
        Wuerfel wuerfel = new Wuerfel();
        feld.setWuerfel(wuerfel);

        assertTrue(feld.isBelegt());
        assertEquals(wuerfel, feld.getWuerfel());
    }

    @Test
    void testWuerfelPlatziereFailWhenOccupied() {
        Wuerfel w1 = new Wuerfel();
        Wuerfel w2 = new Wuerfel();

        feld.wuerfelPlatzieren(w1);
        boolean result = feld.wuerfelPlatzieren(w2);

        assertFalse(result);
        assertEquals(w1, feld.getWuerfel());
    }

}
