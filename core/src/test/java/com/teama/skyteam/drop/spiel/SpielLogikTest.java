package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.flugzeug.*;
import com.teama.skyteam.drop.gui.SchalterGUI;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpielLogikTest {

    private Spiel spiel;
    private SpielLogik spielLogik;
    private Stage stage;
    private Skin skin;
    private SchalterGUI schalterGUI;
    private Image ruder;
    private SzenarioHoeheManager szenarioHoeheManager;

    @BeforeEach
    void setUp() {
        spiel = mock(Spiel.class);
        stage = mock(Stage.class);
        skin = mock(Skin.class);
        schalterGUI = mock(SchalterGUI.class);
        ruder = mock(Image.class);
        szenarioHoeheManager = mock(SzenarioHoeheManager.class);

        Flugzeug flugzeug = mock(Flugzeug.class);
        Flughoehe hoehe = mock(Flughoehe.class);
        ReiseFeld feld = mock(ReiseFeld.class);
        when(spiel.getFlugzeug()).thenReturn(flugzeug);
        when(spiel.getCurrentHoehe()).thenReturn(hoehe);
        when(spiel.getCurrentField()).thenReturn(feld);

        when(flugzeug.getNeigung()).thenReturn(0);
        when(flugzeug.getSpeedAktuell()).thenReturn(0);
        when(flugzeug.getKlappenZustand()).thenReturn(Klappen.LEVEL4);
        Fahrgestell fahrgestell = mock(Fahrgestell.class);
        when(flugzeug.getFahrgestellZustand()).thenReturn(fahrgestell);
        when(fahrgestell.getAkrivierteFahrgestelle()).thenReturn(3);
        when(flugzeug.getBremsenZustand()).thenReturn(Bremsen.LEVEL3);

        when(hoehe.getHoehe()).thenReturn(0);
        when(feld.getAnzahlFlugzeug()).thenReturn(0);
        when(feld.istFlughafen()).thenReturn(true);

        Feld kaffeFeld1 = mock(Feld.class);
        Feld kaffeFeld2 = mock(Feld.class);
        Feld kaffeFeld3 = mock(Feld.class);

        when(spiel.getFeldPilot("kaffeFeld1")).thenReturn(kaffeFeld1);
        when(spiel.getFeldCopilot("kaffeFeld1")).thenReturn(kaffeFeld1);
        when(spiel.getFeldPilot("kaffeFeld2")).thenReturn(kaffeFeld2);
        when(spiel.getFeldCopilot("kaffeFeld2")).thenReturn(kaffeFeld2);
        when(spiel.getFeldPilot("kaffeFeld3")).thenReturn(kaffeFeld3);
        when(spiel.getFeldCopilot("kaffeFeld3")).thenReturn(kaffeFeld3);

        spielLogik = new SpielLogik(spiel, stage, skin, schalterGUI, ruder, szenarioHoeheManager);
    }

    @Test
    void testGewinnBedingungErfuellt() {
        assertTrue(spielLogik.checkGewinnBedingung());
    }

    @Test
    void testGewinnBedingungNichtErfuellt() {
        Flugzeug flugzeug = mock(Flugzeug.class);
        Flughoehe hoehe = mock(Flughoehe.class);

        when(spiel.getFlugzeug()).thenReturn(flugzeug);
        when(spiel.getCurrentHoehe()).thenReturn(hoehe);

        ReiseFeld mockReiseFeld = mock(ReiseFeld.class);
        when(mockReiseFeld.istFlughafen()).thenReturn(false);
        when(mockReiseFeld.getAnzahlFlugzeug()).thenReturn(2);
        when(spiel.getCurrentField()).thenReturn(mockReiseFeld);

        when(flugzeug.getNeigung()).thenReturn(1);
        when(flugzeug.getSpeedAktuell()).thenReturn(70);
        when(flugzeug.getKlappenZustand()).thenReturn(Klappen.LEVEL4);
        Fahrgestell fahrgestell = mock(Fahrgestell.class);
        when(flugzeug.getFahrgestellZustand()).thenReturn(fahrgestell);
        when(fahrgestell.getAkrivierteFahrgestelle()).thenReturn(0);
        when(flugzeug.getBremsenZustand()).thenReturn(Bremsen.LEVEL1);

        when(hoehe.getHoehe()).thenReturn(100);

        assertFalse(spielLogik.checkGewinnBedingung());
    }

    @Test
    void testCheckAusfallBedingungWithKollision() {
        when(spiel.isKollision()).thenReturn(true);
        assertTrue(spielLogik.checkAusfallBedingung());
    }

    @Test
    void testCheckAusfallBedingungMitPositionGroesserAlsRouteGroesse() {
        when(spiel.isKollision()).thenReturn(false);
        when(spiel.getCurrentPosition()).thenReturn(20);
        assertTrue(spielLogik.checkAusfallBedingung());
    }

    @Test
    void testCheckAusfallBedingungMitFlughafenHoehe() {
        ReiseFeld mockReiseFeld = mock(ReiseFeld.class);
        when(mockReiseFeld.istFlughafen()).thenReturn(false);
        when(spiel.getCurrentField()).thenReturn(mockReiseFeld);

        Flughoehe hoehe = mock(Flughoehe.class);
        when(spiel.getCurrentHoehe()).thenReturn(hoehe);
        when(hoehe.getHoehe()).thenReturn(0);
        assertTrue(spielLogik.checkAusfallBedingung());
    }

    @Test
    void testCheckAusfallBedingungMitNeigung() {
        Flugzeug flugzeug = mock(Flugzeug.class);
        when(spiel.getFlugzeug()).thenReturn(flugzeug);
        when(flugzeug.getNeigung()).thenReturn(3);
        assertTrue(spielLogik.checkAusfallBedingung());
    }

    @Test
    void testCheckAusfallBedingungMitFrei() {
        ReiseFeld feld = mock(ReiseFeld.class);
        when(spiel.getCurrentField()).thenReturn(feld);
        when(feld.getAnzahlFlugzeug()).thenReturn(2);
        assertTrue(spielLogik.checkAusfallBedingung());
    }

    @Test
    void testCheckAusfallBedingungMitKeinerBedingungWahr() {
        when(spiel.isKollision()).thenReturn(false);
        ReiseFeld feld = mock(ReiseFeld.class);
        when(spiel.getCurrentField()).thenReturn(feld);
        when(feld.getAnzahlFlugzeug()).thenReturn(0);
        Flughoehe hoehe = mock(Flughoehe.class);
        when(spiel.getCurrentHoehe()).thenReturn(hoehe);
        when(hoehe.getHoehe()).thenReturn(1000);
        assertFalse(spielLogik.checkAusfallBedingung());
    }


    @Test
    void testPilotSchritt() {
        Pilot pilot = mock(Pilot.class);
        Wuerfel gewaehlterWuerfel = mock(Wuerfel.class);
        when(gewaehlterWuerfel.getWert()).thenReturn(3);

        Feld radioFeld = mock(Feld.class);
        when(spiel.getFeldPilot("radioFeld")).thenReturn(radioFeld);
        when(radioFeld.isBelegt()).thenReturn(false);

        TreeMap<Integer, ReiseFeld> routeSzenario = new TreeMap<>();
        ReiseFeld zielFeld = mock(ReiseFeld.class);
        when(zielFeld.getAnzahlFlugzeug()).thenReturn(1);
        routeSzenario.put(2, zielFeld);
        when(spiel.getRouteSzenario()).thenReturn(routeSzenario);
        when(spiel.getCurrentPosition()).thenReturn(0);

        spielLogik.pilotSchritt(pilot, "PRadio", gewaehlterWuerfel, 0);

        verify(pilot).radioWaehlen(gewaehlterWuerfel, 0);
        verify(spiel, atLeastOnce()).getFeldPilot("radioFeld");
    }


    @Test
    void testCopilotSchritt() {
        Copilot copilot = mock(Copilot.class);
        Wuerfel gewaehlterWuerfel = mock(Wuerfel.class);
        when(gewaehlterWuerfel.getWert()).thenReturn(3);

        Feld radioFeld1 = mock(Feld.class);
        Feld radioFeld2 = mock(Feld.class);
        when(spiel.getFeldCopilot("radioFeld1")).thenReturn(radioFeld1);
        when(spiel.getFeldCopilot("radioFeld2")).thenReturn(radioFeld2);
        when(radioFeld1.isBelegt()).thenReturn(false);
        when(radioFeld2.isBelegt()).thenReturn(false);

        TreeMap<Integer, ReiseFeld> routeSzenario = new TreeMap<>();
        ReiseFeld zielFeld = mock(ReiseFeld.class);
        when(zielFeld.getAnzahlFlugzeug()).thenReturn(1);
        routeSzenario.put(2, zielFeld);
        when(spiel.getRouteSzenario()).thenReturn(routeSzenario);
        when(spiel.getCurrentPosition()).thenReturn(0);

        spielLogik.copilotSchritt(copilot, "CRadio1", gewaehlterWuerfel, 0);

        verify(copilot).radioWaehlen(gewaehlterWuerfel, 0);
        verify(spiel, atLeastOnce()).getFeldCopilot("radioFeld1");

        verify(copilot).radioWaehlen(gewaehlterWuerfel, 0);
        verify(spiel).getRouteSzenario();

        verify(zielFeld).removeFlugzeug();
    }

    @Test
    void testFeldZustandAktualisieren() {
        Feld speedFeldPilot = mock(Feld.class);
        Feld speedFeldCopilot = mock(Feld.class);
        when(spiel.getFeldPilot("speedFeld")).thenReturn(speedFeldPilot);
        when(spiel.getFeldCopilot("speedFeld")).thenReturn(speedFeldCopilot);
        when(speedFeldPilot.isBelegt()).thenReturn(true);
        when(speedFeldCopilot.isBelegt()).thenReturn(true);

        Wuerfel w1 = mock(Wuerfel.class);
        Wuerfel w2 = mock(Wuerfel.class);
        when(speedFeldPilot.getWuerfel()).thenReturn(w1);
        when(speedFeldCopilot.getWuerfel()).thenReturn(w2);
        when(w1.getWert()).thenReturn(3);
        when(w2.getWert()).thenReturn(4);

        Flugzeug flugzeug = mock(Flugzeug.class);
        when(spiel.getFlugzeug()).thenReturn(flugzeug);
        Speed speedFive = Speed.FIVE;
        Speed speedEight = Speed.EIGHT;
        when(flugzeug.getSpeedUntereGrenze()).thenReturn(speedFive);
        when(flugzeug.getSpeedObereGrenze()).thenReturn(speedEight);

        when(flugzeug.getSpeedAktuell()).thenReturn(7);

        spielLogik.reiseFeldZustandAktualisieren();

        verify(spiel, atLeastOnce()).getFeldPilot("speedFeld");
        verify(spiel, atLeastOnce()).getFeldCopilot("speedFeld");
        verify(flugzeug).setSpeedAktuell(7);
        verify(spiel).setCurrentSpeed(1);
    }


    @Test
    void testRadio() {
        Wuerfel gewaehlterWuerfel = mock(Wuerfel.class);
        when(gewaehlterWuerfel.getWert()).thenReturn(3);

        TreeMap<Integer, ReiseFeld> routeSzenario = new TreeMap<>();
        ReiseFeld currentField = mock(ReiseFeld.class);
        ReiseFeld targetField = mock(ReiseFeld.class);
        routeSzenario.put(0, currentField);
        routeSzenario.put(2, targetField);
        when(targetField.getAnzahlFlugzeug()).thenReturn(1);

        when(spiel.getRouteSzenario()).thenReturn(routeSzenario);
        when(spiel.getCurrentPosition()).thenReturn(0);

        spielLogik.radio(gewaehlterWuerfel);

        verify(targetField).removeFlugzeug();
    }

    @Test
    void testNeigenSetzen() {
        Feld neigenFeldPilot = mock(Feld.class);
        Feld neigenFeldCopilot = mock(Feld.class);
        when(spiel.getFeldPilot("neigenFeld")).thenReturn(neigenFeldPilot);
        when(spiel.getFeldCopilot("neigenFeld")).thenReturn(neigenFeldCopilot);
        when(neigenFeldPilot.isBelegt()).thenReturn(true);
        when(neigenFeldCopilot.isBelegt()).thenReturn(true);

        Wuerfel w1 = mock(Wuerfel.class);
        Wuerfel w2 = mock(Wuerfel.class);
        when(neigenFeldPilot.getWuerfel()).thenReturn(w1);
        when(neigenFeldCopilot.getWuerfel()).thenReturn(w2);

        Flugzeug flugzeug = mock(Flugzeug.class);
        when(spiel.getFlugzeug()).thenReturn(flugzeug);

        spielLogik.neigenSetzen();

        verify(flugzeug).neigen(anyInt(), any(Wuerfel[].class));
    }


    @Test
    void testVorwaertsBewegen() {
        TreeMap<Integer, ReiseFeld> mockRoute = new TreeMap<>();
        ReiseFeld mockFeld0 = mock(ReiseFeld.class);
        ReiseFeld mockFeld1 = mock(ReiseFeld.class);
        ReiseFeld mockFeld2 = mock(ReiseFeld.class);

        mockRoute.put(0, mockFeld0);
        mockRoute.put(1, mockFeld1);
        mockRoute.put(2, mockFeld2);

        when(spiel.getRouteSzenario()).thenReturn(mockRoute);
        when(spiel.getCurrentPosition()).thenReturn(0);
        when(mockFeld1.getAnzahlFlugzeug()).thenReturn(0);
        when(mockFeld2.getAnzahlFlugzeug()).thenReturn(0);

        spielLogik.vorwaertsBewegen(1);

        verify(spiel).setPosition(1);
        verify(spiel).updateCurrentField();
        verify(szenarioHoeheManager).updateScenario(1);
    }

    @Test
    void testKaffeTokenReturnsFirstTokenAndRemovesIt() {
        ArrayList<KaffeToken> kaffeTokens = new ArrayList<>();
        KaffeToken token1 = mock(KaffeToken.class);
        KaffeToken token2 = mock(KaffeToken.class);

        kaffeTokens.add(token1);
        kaffeTokens.add(token2);

        when(spiel.getKaffeTokens()).thenReturn(kaffeTokens);

        KaffeToken result = spielLogik.kaffeToken();

        assertEquals(token1, result);
        assertEquals(1, kaffeTokens.size());
        assertEquals(token2, kaffeTokens.get(0));
    }

    @Test
    void testNeuWurfTokenReturnsFirstTokenAndRemovesIt() {
        ArrayList<NeuWurfToken> neuWurfTokens = new ArrayList<>();
        NeuWurfToken token1 = mock(NeuWurfToken.class);
        NeuWurfToken token2 = mock(NeuWurfToken.class);

        neuWurfTokens.add(token1);
        neuWurfTokens.add(token2);

        when(spiel.getNeuWurfTokens()).thenReturn(neuWurfTokens);

        NeuWurfToken result = spielLogik.neuWurfToken();

        assertEquals(token1, result);
        assertEquals(1, neuWurfTokens.size());
        assertEquals(token2, neuWurfTokens.get(0));
    }

    @Test
    void testClearManyUseFelds() {

        Feld mockNeigenFeldPilot = mock(Feld.class);
        Feld mockSpeedFeldPilot = mock(Feld.class);
        Feld mockRadioFeldPilot = mock(Feld.class);
        Feld mockNeigenFeldCopilot = mock(Feld.class);
        Feld mockSpeedFeldCopilot = mock(Feld.class);
        Feld mockRadioFeld1Copilot = mock(Feld.class);
        Feld mockRadioFeld2Copilot = mock(Feld.class);

        when(spiel.getFeldPilot("neigenFeld")).thenReturn(mockNeigenFeldPilot);
        when(spiel.getFeldPilot("speedFeld")).thenReturn(mockSpeedFeldPilot);
        when(spiel.getFeldPilot("radioFeld")).thenReturn(mockRadioFeldPilot);

        when(spiel.getFeldCopilot("neigenFeld")).thenReturn(mockNeigenFeldCopilot);
        when(spiel.getFeldCopilot("speedFeld")).thenReturn(mockSpeedFeldCopilot);
        when(spiel.getFeldCopilot("radioFeld1")).thenReturn(mockRadioFeld1Copilot);
        when(spiel.getFeldCopilot("radioFeld2")).thenReturn(mockRadioFeld2Copilot);

        spielLogik.clearManyUseFelds();

        verify(mockNeigenFeldPilot, times(1)).setWuerfel(null);
        verify(mockSpeedFeldPilot, times(1)).setWuerfel(null);
        verify(mockRadioFeldPilot, times(1)).setWuerfel(null);

        verify(mockNeigenFeldCopilot, times(1)).setWuerfel(null);
        verify(mockSpeedFeldCopilot, times(1)).setWuerfel(null);
        verify(mockRadioFeld1Copilot, times(1)).setWuerfel(null);
        verify(mockRadioFeld2Copilot, times(1)).setWuerfel(null);
    }

    @Test
    void testGetOneUseFelds() {


        Feld feld1 = mock(Feld.class);
        Feld feld2 = mock(Feld.class);
        Feld feld3 = mock(Feld.class);
        Feld feld4 = mock(Feld.class);
        Feld feld5 = mock(Feld.class);
        Feld feld6 = mock(Feld.class);
        Feld feld7 = mock(Feld.class);
        Feld feld8 = mock(Feld.class);
        Feld feld9 = mock(Feld.class);
        Feld feld10 = mock(Feld.class);

        when(spiel.getFeldPilot("fahrgestellFeld1_2")).thenReturn(feld1);
        when(spiel.getFeldPilot("fahrgestellFeld3_4")).thenReturn(feld2);
        when(spiel.getFeldPilot("fahrgestellFeld5_6")).thenReturn(feld3);
        when(spiel.getFeldPilot("bremsenFeld2")).thenReturn(feld4);
        when(spiel.getFeldPilot("bremsenFeld4")).thenReturn(feld5);
        when(spiel.getFeldPilot("bremsenFeld6")).thenReturn(feld6);

        when(spiel.getFeldCopilot("klappenFeld1_2")).thenReturn(feld7);
        when(spiel.getFeldCopilot("klappenFeld2_3")).thenReturn(feld8);
        when(spiel.getFeldCopilot("klappenFeld4_5")).thenReturn(feld9);
        when(spiel.getFeldCopilot("klappenFeld5_6")).thenReturn(feld10);



        ArrayList<Feld> oneUseFelds = spielLogik.getOneUseFelds();

        assertNotNull(oneUseFelds);
        assertEquals(10, oneUseFelds.size());

        assertTrue(oneUseFelds.contains(feld1));
        assertTrue(oneUseFelds.contains(feld10));
    }

    @Test
    void testRemoveDiceFromOneUseFelds() {
        Feld belegtesFeld1 = mock(Feld.class);
        Feld belegtesFeld2 = mock(Feld.class);
        Feld freiesFeld = mock(Feld.class);

        when(belegtesFeld1.isBelegt()).thenReturn(true);
        when(belegtesFeld2.isBelegt()).thenReturn(true);
        when(freiesFeld.isBelegt()).thenReturn(false);

        ArrayList<Feld> oneUseFelds = new ArrayList<>();
        oneUseFelds.add(belegtesFeld1);
        oneUseFelds.add(freiesFeld);
        oneUseFelds.add(belegtesFeld2);

        spielLogik.removeDiceFromOneUseFelds(oneUseFelds);

        assertEquals(1, oneUseFelds.size());
        assertTrue(oneUseFelds.contains(freiesFeld));

        verify(belegtesFeld1, times(1)).clearOneUseFeld();
        verify(belegtesFeld2, times(1)).clearOneUseFeld();
        verify(freiesFeld, never()).clearOneUseFeld();
    }



    @Test
    void testHoeheReduzierenRemovesFirstElement() {
        ArrayList<Flughoehe> mockHoeheRoute = new ArrayList<>();
        Flughoehe hoehe1 = mock(Flughoehe.class);
        Flughoehe hoehe2 = mock(Flughoehe.class);

        mockHoeheRoute.add(hoehe1);
        mockHoeheRoute.add(hoehe2);

        when(spiel.getHoeheSzenario()).thenReturn(mockHoeheRoute);

        spielLogik.hoeheReduzieren();

        assertEquals(1, mockHoeheRoute.size());
        assertEquals(hoehe2, mockHoeheRoute.getFirst());
    }

    @Test
    void testHoeheReduzierenDoesNothingWhenOnlyOneElement() {
        ArrayList<Flughoehe> mockHoeheRoute = new ArrayList<>();
        Flughoehe hoehe1 = mock(Flughoehe.class);

        mockHoeheRoute.add(hoehe1);

        when(spiel.getHoeheSzenario()).thenReturn(mockHoeheRoute);

        spielLogik.hoeheReduzieren();

        assertEquals(1, mockHoeheRoute.size());
    }

    @Test
    void testSetNeuWuerfels() {
        Spieler spieler = mock(Spieler.class);

        spielLogik.setNeuWuerfels(spieler);

        verify(spieler, times(1)).setWuerfels(any(ArrayList.class));
    }

    @Test
    void testCheckFinalRoundWhenConditionsMet() {
        ReiseFeld mockField = mock(ReiseFeld.class);
        Flughoehe mockHoehe = mock(Flughoehe.class);

        when(spiel.getCurrentField()).thenReturn(mockField);
        when(mockField.istFlughafen()).thenReturn(true);

        when(spiel.getCurrentHoehe()).thenReturn(mockHoehe);
        when(mockHoehe.getHoehe()).thenReturn(0);

        assertTrue(spielLogik.checkFinalRound());
    }

    @Test
    void testCheckFinalRoundWhenFieldIsNotFlughafen() {

        ReiseFeld mockField = mock(ReiseFeld.class);
        Flughoehe mockHoehe = mock(Flughoehe.class);

        when(spiel.getCurrentField()).thenReturn(mockField);
        when(mockField.istFlughafen()).thenReturn(false);

        when(spiel.getCurrentHoehe()).thenReturn(mockHoehe);
        when(mockHoehe.getHoehe()).thenReturn(0);

        assertFalse(spielLogik.checkFinalRound());
    }

    @Test
    void testCheckFinalRoundWhenHeightIsNotZero() {

        ReiseFeld mockField = mock(ReiseFeld.class);
        Flughoehe mockHoehe = mock(Flughoehe.class);

        when(spiel.getCurrentField()).thenReturn(mockField);
        when(mockField.istFlughafen()).thenReturn(true);

        when(spiel.getCurrentHoehe()).thenReturn(mockHoehe);
        when(mockHoehe.getHoehe()).thenReturn(1000);

        assertFalse(spielLogik.checkFinalRound());
    }


    @Test
    void testClearKaffeFeldWhenKaffeTokenCountIs0() {
        Feld mockKaffeFeld1Pilot = mock(Feld.class);
        Feld mockKaffeFeld1Copilot = mock(Feld.class);
        Feld mockKaffeFeld2Pilot = mock(Feld.class);
        Feld mockKaffeFeld2Copilot = mock(Feld.class);
        Feld mockKaffeFeld3Pilot = mock(Feld.class);
        Feld mockKaffeFeld3Copilot = mock(Feld.class);

        when(spiel.getFeldPilot("Kaffee1")).thenReturn(mockKaffeFeld1Pilot);
        when(spiel.getFeldCopilot("Kaffee1")).thenReturn(mockKaffeFeld1Copilot);
        when(spiel.getFeldPilot("Kaffee2")).thenReturn(mockKaffeFeld2Pilot);
        when(spiel.getFeldCopilot("Kaffee2")).thenReturn(mockKaffeFeld2Copilot);
        when(spiel.getFeldPilot("Kaffee3")).thenReturn(mockKaffeFeld3Pilot);
        when(spiel.getFeldCopilot("Kaffee3")).thenReturn(mockKaffeFeld3Copilot);
        when(spiel.getKaffeTokenCount()).thenReturn(0);

        spielLogik.clearKaffeFeld();

        verify(mockKaffeFeld1Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld1Copilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld2Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld2Copilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld3Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld3Copilot, times(1)).setWuerfel(null);
    }

    @Test
    void testClearKaffeFeldWhenKaffeTokenCountIs1() {
        Feld mockKaffeFeld2Pilot = mock(Feld.class);
        Feld mockKaffeFeld2Copilot = mock(Feld.class);
        Feld mockKaffeFeld3Pilot = mock(Feld.class);
        Feld mockKaffeFeld3Copilot = mock(Feld.class);

        when(spiel.getFeldPilot("Kaffee2")).thenReturn(mockKaffeFeld2Pilot);
        when(spiel.getFeldCopilot("Kaffee2")).thenReturn(mockKaffeFeld2Copilot);
        when(spiel.getFeldPilot("Kaffee3")).thenReturn(mockKaffeFeld3Pilot);
        when(spiel.getFeldCopilot("Kaffee3")).thenReturn(mockKaffeFeld3Copilot);
        when(spiel.getKaffeTokenCount()).thenReturn(1);

        spielLogik.clearKaffeFeld();

        verify(mockKaffeFeld2Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld2Copilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld3Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld3Copilot, times(1)).setWuerfel(null);
    }

    @Test
    void testClearKaffeFeldWhenKaffeTokenCountIs2() {
        Feld mockKaffeFeld3Pilot = mock(Feld.class);
        Feld mockKaffeFeld3Copilot = mock(Feld.class);

        when(spiel.getFeldPilot("Kaffee3")).thenReturn(mockKaffeFeld3Pilot);
        when(spiel.getFeldCopilot("Kaffee3")).thenReturn(mockKaffeFeld3Copilot);
        when(spiel.getKaffeTokenCount()).thenReturn(2);

        spielLogik.clearKaffeFeld();

        verify(mockKaffeFeld3Pilot, times(1)).setWuerfel(null);
        verify(mockKaffeFeld3Copilot, times(1)).setWuerfel(null);
    }

}
