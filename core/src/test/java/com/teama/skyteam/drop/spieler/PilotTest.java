package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.KaffeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PilotTest {

    private Pilot pilot;
    private Spiel mockSpiel;
    private Wuerfel mockWuerfel;

    @BeforeEach
    void setUp() {
        mockSpiel = mock(Spiel.class);
        pilot = new Pilot(mockSpiel);
        mockWuerfel = mock(Wuerfel.class);
    }

    @Test
    void testSpeedWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("speedFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.speedWaehlen(mockWuerfel, 0);
        verify(mockSpiel, times(1)).getFeldPilot("speedFeld");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testSpeedWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("speedFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.speedWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld 'speedFeld' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testRadioWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("radioFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.radioWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldPilot("radioFeld");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testRadioWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("radioFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.radioWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld 'radioFeld' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testNeigenWaehlenSuccess() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("neigenFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.neigenWaehlen(mockWuerfel, 0);

        verify(mockSpiel, times(1)).getFeldPilot("neigenFeld");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testNeigenWaehlenFail() {
        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("neigenFeld")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(false);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.neigenWaehlen(mockWuerfel, 0)
        );

        assertEquals("Feld 'neigenFeld' ist schon belegt!", exception.getMessage());
    }

    @Test
    void testKaffeeWaehlenMaxTokens() {
        when(mockSpiel.getKaffeTokenCount()).thenReturn(3);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.kaffeeWaehlen(mockWuerfel, 0, "Kaffee1")
        );

        assertEquals("Sie können nicht mehr als 3 KaffeTokens haben!", exception.getMessage());
    }

    @Test
    void testBremsenWaehlenWert2() {
        when(mockWuerfel.getWert()).thenReturn(2);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("bremsenFeld2")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.bremsenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("bremsenFeld2");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testBremsenWaehlenWert4() {
        when(mockWuerfel.getWert()).thenReturn(4);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("bremsenFeld4")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.bremsenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("bremsenFeld4");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testBremsenWaehlenWert6() {
        when(mockWuerfel.getWert()).thenReturn(6);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("bremsenFeld6")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.bremsenWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("bremsenFeld6");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testBremsenWaehlenInvalidValue() {
        when(mockWuerfel.getWert()).thenReturn(5);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.bremsenWaehlen(mockWuerfel, 0)
        );

        assertEquals("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!", exception.getMessage());

        assertEquals(1, pilot.getWuerfels().size());
    }

    @Test
    void testFahrgestellWaehlenWert1() {
        when(mockWuerfel.getWert()).thenReturn(1);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("fahrgestellFeld1_2")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.fahrgestellWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("fahrgestellFeld1_2");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testFahrgestellWaehlenWert3() {
        when(mockWuerfel.getWert()).thenReturn(3);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("fahrgestellFeld3_4")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.fahrgestellWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("fahrgestellFeld3_4");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testFahrgestellWaehlenWert5() {
        when(mockWuerfel.getWert()).thenReturn(5);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("fahrgestellFeld5_6")).thenReturn(mockFeld);
        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.fahrgestellWaehlen(mockWuerfel, 0);

        verify(mockSpiel).getFeldPilot("fahrgestellFeld5_6");
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testFahrgestellWaehlenInvalidValue() {
        when(mockWuerfel.getWert()).thenReturn(7);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.fahrgestellWaehlen(mockWuerfel, 0)
        );

        assertEquals("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!", exception.getMessage());
        assertEquals(1, pilot.getWuerfels().size());
    }

    @Test
    void testFahrgestellWaehlenFieldAlreadyOccupied() {
        when(mockWuerfel.getWert()).thenReturn(1);

        Feld mockFeld = mock(Feld.class);
        when(mockSpiel.getFeldPilot("fahrgestellFeld1_2")).thenReturn(mockFeld);

        when(mockFeld.wuerfelPlatzieren(mockWuerfel)).thenReturn(true);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.fahrgestellWaehlen(mockWuerfel, 0);

        verify(mockFeld, times(1)).wuerfelPlatzieren(mockWuerfel);

        Wuerfel mockWuerfel2 = mock(Wuerfel.class);
        when(mockWuerfel2.getWert()).thenReturn(2);

        wuerfels.add(mockWuerfel2);

        when(mockFeld.wuerfelPlatzieren(mockWuerfel2)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.fahrgestellWaehlen(mockWuerfel2, 0)
        );

        assertEquals("Feld 'fahrgestellFeld1_2' ist schon belegt!", exception.getMessage());
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
        pilot.setWuerfels(wuerfels);

        pilot.kaffeeWaehlen(mockWuerfel, 0, "Kaffee1");

        verify(mockSpiel, times(1)).kaffeTokenAnzahlErhoehen(any(KaffeToken.class));
        assertTrue(pilot.getWuerfels().isEmpty());
    }

    @Test
    void testGetAnzahlWuerfels() {
        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mock(Wuerfel.class));
        wuerfels.add(mock(Wuerfel.class));

        pilot.setWuerfels(wuerfels);

        assertEquals(2, pilot.getAnzahlWuerfels());
    }

    @Test
    void testNeuWurfTokenBenutzenSingleTarget() {
        NeuWurfToken mockToken = mock(NeuWurfToken.class);

        ArrayList<Wuerfel> initialWuerfels = new ArrayList<>();
        initialWuerfels.add(mockWuerfel);

        ArrayList<Wuerfel> newWuerfels = new ArrayList<>();
        Wuerfel newMockWuerfel = mock(Wuerfel.class);
        newWuerfels.add(newMockWuerfel);

        when(mockToken.erneutWerfen(pilot)).thenReturn(newWuerfels);

        pilot.setWuerfels(initialWuerfels);

        pilot.neuWurfTokenBenutzen(mockToken, pilot);

        assertEquals(newWuerfels, pilot.getWuerfels());
        verify(mockToken, times(1)).erneutWerfen(pilot);
    }

    @Test
    void testNeuWurfTokenBenutzenTwoTargets() {
        NeuWurfToken mockToken = mock(NeuWurfToken.class);

        Pilot targetPilot = new Pilot(mockSpiel);
        Copilot targetCopilot = new Copilot(mockSpiel);

        ArrayList<Wuerfel> newWuerfels1 = new ArrayList<>();
        ArrayList<Wuerfel> newWuerfels2 = new ArrayList<>();
        newWuerfels1.add(mock(Wuerfel.class));
        newWuerfels2.add(mock(Wuerfel.class));

        when(mockToken.erneutWerfen(targetPilot)).thenReturn(newWuerfels1);
        when(mockToken.erneutWerfen(targetCopilot)).thenReturn(newWuerfels2);

        pilot.neuWurfTokenBenutzen(mockToken, targetPilot, targetCopilot);

        assertEquals(newWuerfels1, targetPilot.getWuerfels());
        assertEquals(newWuerfels2, targetCopilot.getWuerfels());

        verify(mockToken, times(1)).erneutWerfen(targetPilot);
        verify(mockToken, times(1)).erneutWerfen(targetPilot);
    }

    @Test
    void testKaffeeTokenBenutzenPlusSuccess() {
        KaffeToken mockToken = mock(KaffeToken.class);
        Wuerfel mockWuerfel = mock(Wuerfel.class);
        Wuerfel updatedWuerfel = mock(Wuerfel.class);

        when(mockWuerfel.getWert()).thenReturn(5);

        when(mockToken.wertAendern(KaffeTokenAktion.PLUS, mockWuerfel)).thenReturn(updatedWuerfel);

        when(updatedWuerfel.getWert()).thenReturn(6);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.kaffeeTokenBenutzen(mockToken, 0, KaffeTokenAktion.PLUS);

        Wuerfel resultWuerfel = pilot.getWuerfels().get(0);
        assertEquals(updatedWuerfel, resultWuerfel);

        assertEquals(6, resultWuerfel.getWert());

        verify(mockToken, times(1)).wertAendern(KaffeTokenAktion.PLUS, mockWuerfel);
    }

    @Test
    void testKaffeeTokenBenutzenMinusSuccess() {
        KaffeToken mockToken = mock(KaffeToken.class);
        Wuerfel mockWuerfel = mock(Wuerfel.class);
        Wuerfel updatedWuerfel = mock(Wuerfel.class);

        when(mockWuerfel.getWert()).thenReturn(5);

        when(mockToken.wertAendern(KaffeTokenAktion.MINUS, mockWuerfel)).thenReturn(updatedWuerfel);

        when(updatedWuerfel.getWert()).thenReturn(4);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        pilot.kaffeeTokenBenutzen(mockToken, 0, KaffeTokenAktion.MINUS);

        Wuerfel resultWuerfel = pilot.getWuerfels().get(0);
        assertEquals(updatedWuerfel, resultWuerfel);

        assertEquals(4, resultWuerfel.getWert());

        verify(mockToken, times(1)).wertAendern(KaffeTokenAktion.MINUS, mockWuerfel);
    }

    @Test
    void testKaffeeTokenBenutzenPlusException() {
        KaffeToken mockToken = mock(KaffeToken.class);
        Wuerfel mockWuerfel = mock(Wuerfel.class);

        when(mockWuerfel.getWert()).thenReturn(6);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.kaffeeTokenBenutzen(mockToken, 0, KaffeTokenAktion.PLUS)
        );

        assertEquals("Wuerfelwert ist schon maximal", exception.getMessage());
    }

    @Test
    void testKaffeeTokenBenutzenMinusException() {
        KaffeToken mockToken = mock(KaffeToken.class);
        Wuerfel mockWuerfel = mock(Wuerfel.class);

        when(mockWuerfel.getWert()).thenReturn(1);

        ArrayList<Wuerfel> wuerfels = new ArrayList<>();
        wuerfels.add(mockWuerfel);
        pilot.setWuerfels(wuerfels);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pilot.kaffeeTokenBenutzen(mockToken, 0, KaffeTokenAktion.MINUS)
        );

        assertEquals("Wuerfelwert ist schon minimal", exception.getMessage());
    }

}
