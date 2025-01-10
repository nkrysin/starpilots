package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.gui.SpielGUI;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.mockito.Mockito.*;


public class SpielControllerGUITest {

    private Spiel spiel;
    private SpielLogik spielLogik;
    private SzenarioHoeheManager szenarioHoeheManager;
    private SpielGUI spielGUI;
    private Stage stage;
    private Skin skin;
    private Spieler pilot;
    private Spieler copilot;
    private SpielControllerGUI spielControllerGUI;


    @BeforeEach
    void setUp() throws Exception {
        spiel = mock(Spiel.class);
        spielLogik = mock(SpielLogik.class);
        szenarioHoeheManager = mock(SzenarioHoeheManager.class);
        spielGUI = mock(SpielGUI.class);
        stage = mock(Stage.class);
        skin = mock(Skin.class);
        pilot = mock(Pilot.class);
        copilot = mock(Copilot.class);

        when(spiel.getPilot()).thenReturn((Pilot) pilot);
        when(spiel.getCopilot()).thenReturn((Copilot) copilot);

        spielControllerGUI = new SpielControllerGUI(spiel, szenarioHoeheManager, stage, skin);

        injectMock(spielControllerGUI, "spielLogik", spielLogik);
        injectMock(spielControllerGUI, "spielGUI", spielGUI);
    }

    private void injectMock(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, mock);
    }

    @Test
    void testSpielenSchrittPrepareRound() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(false);
        when(spiel.getRunde()).thenReturn(1);

        spielControllerGUI.spielenSchritt();
    }

    @Test
    void testSpielenSchrittPlayTurnFirst() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(false);
        when(spiel.getRunde()).thenReturn(1);
        when(pilot.getAnzahlWuerfels()).thenReturn(2);
        when(spielLogik.neigenSetzen()).thenReturn(false);
        when(spielLogik.reiseFeldZustandAktualisieren()).thenReturn(false);

        spielControllerGUI.spielenSchritt();
    }

    @Test
    void testSpielenSchrittPlayTurnSecond() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(false);
        when(spiel.getRunde()).thenReturn(1);
        when(copilot.getAnzahlWuerfels()).thenReturn(2);
        when(spielLogik.neigenSetzen()).thenReturn(false);
        when(spielLogik.reiseFeldZustandAktualisieren()).thenReturn(false);

        spielControllerGUI.spielenSchritt();
    }

    @Test
    void testSpielenSchrittFinishRound() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(false);
        when(spiel.getRunde()).thenReturn(1);
        when(pilot.getAnzahlWuerfels()).thenReturn(0);
        when(copilot.getAnzahlWuerfels()).thenReturn(0);
        when(spielLogik.neigenSetzen()).thenReturn(true);
        when(spielLogik.reiseFeldZustandAktualisieren()).thenReturn(true);
        when(spielLogik.checkGewinnBedingung()).thenReturn(true);

        spielControllerGUI.spielenSchritt();
    }

    @Test
    void testSpielenSchrittFinalCheckWin() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(true);
        when(spiel.getRunde()).thenReturn(1);
        when(pilot.getAnzahlWuerfels()).thenReturn(0);
        when(copilot.getAnzahlWuerfels()).thenReturn(0);
        when(spielLogik.neigenSetzen()).thenReturn(true);
        when(spielLogik.reiseFeldZustandAktualisieren()).thenReturn(true);
        when(spielLogik.checkGewinnBedingung()).thenReturn(true);

        spielControllerGUI.spielenSchritt();
    }

    @Test
    void testSpielenSchrittFinalCheckLose() {
        when(spielLogik.checkAusfallBedingung()).thenReturn(false);
        when(spielLogik.checkFinalRound()).thenReturn(true);
        when(spiel.getRunde()).thenReturn(1);
        when(pilot.getAnzahlWuerfels()).thenReturn(0);
        when(copilot.getAnzahlWuerfels()).thenReturn(0);
        when(spielLogik.neigenSetzen()).thenReturn(true);
        when(spielLogik.reiseFeldZustandAktualisieren()).thenReturn(true);
        when(spielLogik.checkGewinnBedingung()).thenReturn(false);

        spielControllerGUI.spielenSchritt();
    }

}
