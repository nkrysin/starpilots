package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeeToken;
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
        when(mockSpiel.getFeldCopilot(FeldName.SPEED)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.speedWaehlen(mockWuerfel, 0);
        verify(mockSpiel, times(1)).getFeldCopilot(FeldName.SPEED);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testSpeedWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot(FeldName.SPEED)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.speedWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld '" + FeldName.SPEED + "' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testNeigenWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot(FeldName.NEIGEN)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.neigenWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldCopilot(FeldName.NEIGEN);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testNeigenWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot(FeldName.NEIGEN)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> copilot.neigenWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld '" + FeldName.NEIGEN + "' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testKaffeeWaehlenMaxTokens() {
        when(mockSpiel.getKaffeeTokenCount()).thenReturn(3);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> copilot.kaffeeWaehlen(mockWuerfel, 0, FeldName.KAFFEE_1)
        );

        assertEquals("Sie können nicht mehr als 3 KaffeeTokens haben!", exception.getMessage());
    }

    @Test
    void testKaffeeWaehlenSuccess() {
        when(mockSpiel.getKaffeeTokenCount()).thenReturn(0);

        Feld mockKaffeeFeld1Pilot = mock(Feld.class);

        when(mockSpiel.getFeldPilot(FeldName.KAFFEE_1)).thenReturn(mockKaffeeFeld1Pilot);

        Feld mockKaffeeFeld1Copilot = mock(Feld.class);

        when(mockSpiel.getFeldCopilot(FeldName.KAFFEE_1)).thenReturn(mockKaffeeFeld1Copilot);

        when(mockKaffeeFeld1Pilot.isBelegt()).thenReturn(false);
        when(mockKaffeeFeld1Copilot.isBelegt()).thenReturn(false);

        when(mockKaffeeFeld1Pilot.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);
        when(mockKaffeeFeld1Copilot.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.kaffeeWaehlen(mockWuerfel, 0, FeldName.KAFFEE_1);

        verify(mockSpiel, times(1)).kaffeeTokenAnzahlErhoehen(any(KaffeeToken.class));
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert1() {
        when(mockWuerfel.getWert()).thenReturn(1);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_1_2)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot(FeldName.KLAPPEN_1_2);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert2FieldOccupied() {
        when(mockWuerfel.getWert()).thenReturn(2);

        Feld mockFeld1_2 = mock(Feld.class);
        Feld mockFeld2_3 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_1_2)).thenReturn(mockFeld1_2);
        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_2_3)).thenReturn(mockFeld2_3);

        when(mockFeld1_2.isBelegt()).thenReturn(true);

        when(mockFeld2_3.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot(FeldName.KLAPPEN_1_2);
        verify(mockSpiel).getFeldCopilot(FeldName.KLAPPEN_2_3);
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
        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_4_5)).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(2)).getFeldCopilot(FeldName.KLAPPEN_4_5);

        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testKlappenWaehlenWert5FieldOccupied() {
        when(mockWuerfel.getWert()).thenReturn(5);

        Feld mockFeld4_5 = mock(Feld.class);
        Feld mockFeld5_6 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_4_5)).thenReturn(mockFeld4_5);
        when(mockSpiel.getFeldCopilot(FeldName.KLAPPEN_5_6)).thenReturn(mockFeld5_6);

        when(mockFeld4_5.isBelegt()).thenReturn(true);

        when(mockFeld5_6.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.klappenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldCopilot(FeldName.KLAPPEN_4_5);
        verify(mockSpiel).getFeldCopilot(FeldName.KLAPPEN_5_6);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenFeld1Success() {
        Feld mockRadioFeld1 = mock(Feld.class);
        when(mockSpiel.getFeldCopilot(FeldName.RADIO_COPILOT_1)).thenReturn(mockRadioFeld1);
        when(mockRadioFeld1.isBelegt()).thenReturn(false);
        when(mockRadioFeld1.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.radioWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(2)).getFeldCopilot(FeldName.RADIO_COPILOT_1);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenFeld2Success() {
        Feld mockRadioFeld1 = mock(Feld.class);
        Feld mockRadioFeld2 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot(FeldName.RADIO_COPILOT_1)).thenReturn(mockRadioFeld1);
        when(mockSpiel.getFeldCopilot(FeldName.RADIO_COPILOT_2)).thenReturn(mockRadioFeld2);

        when(mockRadioFeld1.isBelegt()).thenReturn(true);
        when(mockRadioFeld2.isBelegt()).thenReturn(false);
        when(mockRadioFeld2.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        copilot.setWuerfels(wuerfels);

        copilot.radioWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldCopilot(FeldName.RADIO_COPILOT_1);
        verify(mockSpiel, times(2)).getFeldCopilot(FeldName.RADIO_COPILOT_2);
        assertTrue(copilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenBothFieldsOccupied() {
        Feld mockRadioFeld1 = mock(Feld.class);
        Feld mockRadioFeld2 = mock(Feld.class);

        when(mockSpiel.getFeldCopilot(FeldName.RADIO_COPILOT_1)).thenReturn(mockRadioFeld1);
        when(mockSpiel.getFeldCopilot(FeldName.RADIO_COPILOT_2)).thenReturn(mockRadioFeld2);

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
