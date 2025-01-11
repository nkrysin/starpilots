package com.teama.skyteam.drop.flugzeug;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FahrgestellTest {

    private Fahrgestell fahrgestell;

    @BeforeEach
    void setUp() {
        fahrgestell = new Fahrgestell();
    }

    @Test
    void testGetAkrivierteFahrgestelleWithZero() {
        assertEquals(0, fahrgestell.getAkrivierteFahrgestelle());
    }

    @Test
    void testFahrgestellAktivierenWennFrei() {
        assertTrue(fahrgestell.fahrgestellAktivieren(2));
        assertEquals(1, fahrgestell.getAkrivierteFahrgestelle());
    }

    @Test
    void testFahrgestellAktivierenWennBelegt() {
        fahrgestell.fahrgestellAktivieren(2);
        assertEquals(1, fahrgestell.getAkrivierteFahrgestelle());
        assertFalse(fahrgestell.fahrgestellAktivieren(2));
    }
}
