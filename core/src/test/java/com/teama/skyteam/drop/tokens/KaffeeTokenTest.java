package com.teama.skyteam.drop.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
class KaffeeTokenTest {
    private Wuerfel w;

    @BeforeEach
    void setUp() {
        w = new Wuerfel();
    }

    @Test
    void wertAendernIncrement() {
        int wert = w.getWert();
        w.wertAendern(KaffeeTokenAktion.PLUS);
        assertEquals(wert + 1, w.getWert());
    }

    @Test
    void wertAendernDecrement() {
        int wert = w.getWert();
        w.wertAendern(KaffeeTokenAktion.MINUS);
        assertEquals(wert - 1, w.getWert());
    }
    @Test
    void testWertAendernPlus() {
        KaffeeToken kaffeeToken = new KaffeeToken();
        Wuerfel mockWuerfel = mock(Wuerfel.class);
        KaffeeTokenAktion aktion = KaffeeTokenAktion.PLUS;
        Wuerfel result = kaffeeToken.wertAendern(aktion, mockWuerfel);
        verify(mockWuerfel, times(1)).wertAendern(KaffeeTokenAktion.PLUS);
        assertEquals(mockWuerfel, result);
    }

    @Test
    void testWertAendernMinus() {
        KaffeeToken kaffeeToken = new KaffeeToken();
        Wuerfel mockWuerfel = mock(Wuerfel.class);
        KaffeeTokenAktion aktion = KaffeeTokenAktion.MINUS;
        Wuerfel result = kaffeeToken.wertAendern(aktion, mockWuerfel);
        verify(mockWuerfel, times(1)).wertAendern(KaffeeTokenAktion.MINUS);
        assertEquals(mockWuerfel, result);
    }

}
