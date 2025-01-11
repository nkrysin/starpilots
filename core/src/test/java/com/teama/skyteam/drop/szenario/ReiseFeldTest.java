package com.teama.skyteam.drop.szenario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReiseFeldTest {

    @Test
    void testConstructor() {
        ReiseFeld reisefeld = new ReiseFeld(5, false);

        assertEquals(5, reisefeld.getAnzahlFlugzeug());
        assertFalse(reisefeld.istFlughafen());
    }

    @Test
    void testConstructorWithFlughafen() {
        ReiseFeld reisefeld = new ReiseFeld(3, true);

        assertEquals(3, reisefeld.getAnzahlFlugzeug());
        assertTrue(reisefeld.istFlughafen());
    }

    @Test
    void testRemoveFlugzeug() {
        ReiseFeld reisefeld = new ReiseFeld(2, false);

        reisefeld.removeFlugzeug();

        assertEquals(1, reisefeld.getAnzahlFlugzeug());
    }

    @Test
    void testSetAnzahlFlugzeug() {
        ReiseFeld reisefeld = new ReiseFeld(0, false);

        reisefeld.setAnzahlFlugzeug(10);

        assertEquals(10, reisefeld.getAnzahlFlugzeug());
    }


}
