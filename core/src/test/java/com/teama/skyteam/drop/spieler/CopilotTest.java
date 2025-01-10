package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class CopilotTest {

    private Copilot copilot;
    private Spiel mockSpiel;
    private Wuerfel mockWuerfel;

    @BeforeEach
    void setUp() {
        mockSpiel = mock(Spiel.class);
        copilot = new Copilot(mockSpiel);
        mockWuerfel = mock(Wuerfel.class);
    }

    @Test
    void testSpeedWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("speedFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.speedWaehlen(mockWuerfel, 0);
        verify(mockSpiel, times(1)).getFeldCopilot("speedFeld");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testSpeedWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("speedFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.speedWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld 'speedFeld' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testNeigenWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("neigenFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.neigenWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldCopilot("neigenFeld");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testNeigenWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("neigenFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.neigenWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld 'neigenFeld' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testKaffeeWaehlenMaxTokens() {
        when(mockSpiel.getKaffeTokenCount()).thenReturn(3);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> copilot.kaffeeWaehlen(mockWuerfel, 0, "Kaffee1")
        );

        assertEquals("Sie können nicht mehr als 3 KaffeTokens haben!", exception.getMessage());
    }

    @Test
    void testKaffeeWaehlenSuccess() {
        when(mockSpiel.getKaffeTokenCount()).thenReturn(0);

        Feld mockKaffeFeld1Pilot = mock(Feld.class);

        when(mockSpiel.getFeldPilot("Kaffee1")).thenReturn(mockKaffeFeld1Pilot);

        Feld mockKaffeFeld1Copilot = mock(Feld.class);

        when(mockSpiel.getFeldCopilot("Kaffee1")).thenReturn(mockKaffeFeld1Copilot);

        when(mockKaffeFeld1Pilot.isBelegt()).thenReturn(false);
        when(mockKaffeFeld1Copilot.isBelegt()).thenReturn(false);

        when(mockKaffeFeld1Pilot.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);
        when(mockKaffeFeld1Copilot.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.kaffeeWaehlen(mockWuerfel, 0, "Kaffee1");

        verify(mockSpiel, times(1)).kaffeTokenAnzahlErhoehen(any(KaffeToken.class));
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert1() {
        when(mockWuerfel.getWert()).thenReturn(1);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("klappenFeld1_2")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot("klappenFeld1_2");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert2FieldOccupied() {
        when(mockWuerfel.getWert()).thenReturn(2);

        Feld mockFeld1_2 = mock(Feld.class);
        Feld mockFeld2_3 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot("klappenFeld1_2")).thenReturn(mockFeld1_2);
        when(mockSpiel.getFeldCopilot("klappenFeld2_3")).thenReturn(mockFeld2_3);

        when(mockFeld1_2.isBelegt()).thenReturn(true);

        when(mockFeld2_3.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot("klappenFeld1_2");
        verify(mockSpiel).getFeldCopilot("klappenFeld2_3");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenInvalidValue() {
        when(mockWuerfel.getWert()).thenReturn(7);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.klappenWaehlen(mockWuerfel, 0)
        );

        assertEquals("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!", exception.getMessage());
    }

    @Test
    void testKlappenWaehlenWert5() {
        when(mockWuerfel.getWert()).thenReturn(5);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("klappenFeld4_5")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(2)).getFeldCopilot("klappenFeld4_5");

        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert5FieldOccupied() {
        when(mockWuerfel.getWert()).thenReturn(5);

        Feld mockFeld4_5 = mock(Feld.class);
        Feld mockFeld5_6 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot("klappenFeld4_5")).thenReturn(mockFeld4_5);
        when(mockSpiel.getFeldCopilot("klappenFeld5_6")).thenReturn(mockFeld5_6);

        when(mockFeld4_5.isBelegt()).thenReturn(true);

        when(mockFeld5_6.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot("klappenFeld4_5");
        verify(mockSpiel).getFeldCopilot("klappenFeld5_6");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenFeld1Success() {
        Feld mockRadioFeld1 = mock(Feld.class);
        when(mockSpiel.getFeldCopilot("radioFeld1")).thenReturn(mockRadioFeld1);
        when(mockRadioFeld1.isBelegt()).thenReturn(false);
        when(mockRadioFeld1.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.radioWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(2)).getFeldCopilot("radioFeld1");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenFeld2Success() {
        Feld mockRadioFeld1 = mock(Feld.class);
        Feld mockRadioFeld2 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot("radioFeld1")).thenReturn(mockRadioFeld1);
        when(mockSpiel.getFeldCopilot("radioFeld2")).thenReturn(mockRadioFeld2);

        when(mockRadioFeld1.isBelegt()).thenReturn(true);
        when(mockRadioFeld2.isBelegt()).thenReturn(false);
        when(mockRadioFeld2.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.radioWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldCopilot("radioFeld1");
        verify(mockSpiel, times(2)).getFeldCopilot("radioFeld2");
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenBothFieldsOccupied() {
        Feld mockRadioFeld1 = mock(Feld.class);
        Feld mockRadioFeld2 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot("radioFeld1")).thenReturn(mockRadioFeld1);
        when(mockSpiel.getFeldCopilot("radioFeld2")).thenReturn(mockRadioFeld2);

        when(mockRadioFeld1.isBelegt()).thenReturn(true);
        when(mockRadioFeld2.isBelegt()).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.radioWaehlen(mockWuerfel, 0)
        );

        assertEquals("Beide Radiofelder sind bereits belegt!", exception.getMessage());
        assertEquals(1, copilot.getWuerfels().size());
    }

}
