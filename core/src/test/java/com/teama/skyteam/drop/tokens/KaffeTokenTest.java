package com.teama.skyteam.drop.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class KaffeTokenTest {
    private Wuerfel w;

    @BeforeEach
    void setUp() {
        w = new Wuerfel();
    }

    @Test
    void wertAendernIncrement() {
        int wert = w.getWert();
        w.wertAendern(KaffeTokenAktion.PLUS);
        assertEquals(wert + 1, w.getWert());
    }

    @Test
    void wertAendernDecrement() {
        int wert = w.getWert();
        w.wertAendern(KaffeTokenAktion.MINUS);
        assertEquals(wert - 1, w.getWert());
    }
}
