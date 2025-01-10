package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.Route;
import com.teama.skyteam.drop.tokens.KaffeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Field;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpielTest {
    private Files originalFiles;

    @BeforeEach
    public void setup() throws Exception {
        Field filesField = Gdx.class.getDeclaredField("files");
        filesField.setAccessible(true);

        originalFiles = (Files) filesField.get(null);

        Files mockFiles = mock(Files.class);

        FileHandle mockFileHandle = mock(FileHandle.class);

        String mockedContent = "MontrealTrudeau\nGREEN\n7\n0\n0\n1\n2\n1\n3\n2\nHaneda\nGREEN\n8\n0\n1\n1\n2\n1\n0\n2\n1\nKualaLumpur\nGREEN\n8\n0\n0\n1\n1\n0\n1\n1\n1\nSzenarioType\n";

        when(mockFileHandle.readString()).thenReturn(mockedContent);
        when(mockFileHandle.exists()).thenReturn(true);

        when(mockFiles.internal("routes.txt")).thenReturn(mockFileHandle);

        filesField.set(null, mockFiles);
    }

    @AfterEach
    public void tearDown() throws Exception {
        Field filesField = Gdx.class.getDeclaredField("files");
        filesField.setAccessible(true);
        filesField.set(null, originalFiles);
    }

    @Test
    void testKaffeTokenAnzahlErhoehen() {
        Spiel spiel = new Spiel();
        int initialCount = spiel.getKaffeTokenCount();


        spiel.kaffeTokenAnzahlErhoehen(new KaffeToken());
        assertEquals(initialCount + 1, spiel.getKaffeTokenCount());
    }

    @Test
    void testNeuWurfTokenAnzahlErhoehen() {
        Spiel spiel = new Spiel();
        int initialCount = spiel.getNeuWurfTokenCount();

        spiel.neuWurfTokenAnzahlErhoehen();
        assertEquals(initialCount + 1, spiel.getNeuWurfTokenCount());
    }

    @Test
    void testSetPilotFelds() {
        Spiel spiel = new Spiel();
        spiel.setPilotFelds();

        assertNotNull(spiel.getFeldPilot("radioFeld"));
        assertNotNull(spiel.getFeldPilot("Kaffee1"));
    }

    @Test
    void testSetCopilotFelds() {
        Spiel spiel = new Spiel();
        spiel.setCopilotFelds();

        assertNotNull(spiel.getFeldCopilot("radioFeld1"));
        assertNotNull(spiel.getFeldCopilot("Kaffee2"));
    }

    @Test
    void testUpdateCurrentField() {
        Spiel spiel = new Spiel();
        spiel.setSzenario(Route.HANEDA);
        spiel.setPosition(0);

        spiel.updateCurrentField();
        assertNotNull(spiel.getCurrentField());
    }

    @Test
    void testUpdateCurrentHoehe() {
        Spiel spiel = new Spiel();
        spiel.setSzenario(Route.HANEDA);

        spiel.updateCurrentHoehe();
        assertNotNull(spiel.getCurrentHoehe());
    }

    @Test
    void testSetAndGetKollision() {
        Spiel spiel = new Spiel();
        spiel.setKollision(true);

        assertTrue(spiel.isKollision());
    }


    @Test
    void testSetAndGetPosition() {
        Spiel spiel = new Spiel();
        spiel.setPosition(5);

        assertEquals(5, spiel.getCurrentPosition());
    }

    @Test
    void testSetRouteSzenario() {
        Spiel spiel = new Spiel();
        spiel.setSzenario(Route.MONTREAL_TRUDEAU);

        spiel.setRouteSzenario();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();

        assertNotNull(routeSzenario);
        assertFalse(routeSzenario.isEmpty());
    }

    @Test
    void testSetHoeheSzenarioWithEmptyList() {
        Spiel spiel = new Spiel();

        spiel.setSzenario(Route.HANEDA);

        spiel.setHoeheSzenario();
        assertNotNull(spiel.getHoeheSzenario());
        assertFalse(spiel.getHoeheSzenario().isEmpty());
    }

}
