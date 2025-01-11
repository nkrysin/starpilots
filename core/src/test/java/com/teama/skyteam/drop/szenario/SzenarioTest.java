package com.teama.skyteam.drop.szenario;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class SzenarioTest {

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

    @Test
    void testConstructorWithValidRoute() throws IOException {
        Szenario szenario = new Szenario(Route.MONTREAL_TRUDEAU);

        assertNotNull(szenario.getCurrentRoute());
        assertFalse(szenario.getCurrentRoute().isEmpty());
    }

    @Test
    void testConstructorWithNullRoute() {
        assertThrows(IOException.class, () -> new Szenario(null));
    }

    @Test
    void testGetCurrentRoute() throws IOException {
        Szenario szenario = new Szenario(Route.HANEDA);
        TreeMap<Integer, ReiseFeld> route = szenario.getCurrentRoute();

        assertNotNull(route);
        assertFalse(route.isEmpty());
    }

    @Test
    void testGetHoeheSzenarioType() throws IOException {
        Szenario szenario = new Szenario(Route.MONTREAL_TRUDEAU);

        HoeheSzenarioTypes type = szenario.getHoeheSzenarioType();
        assertNotNull(type);
        assertEquals(HoeheSzenarioTypes.GREEN, type);
    }

    @Test
    void testSetHoeheSzenarioWithValidScenario() throws IOException {
        Szenario szenario = new Szenario(Route.MONTREAL_TRUDEAU);
        assertNotNull(szenario.getHoeheSzenario());
        assertFalse(szenario.getHoeheSzenario().isEmpty());
    }

    @Test
    void testGetCurrentFlughoeheWithValidScenario() throws IOException {
        Szenario szenario = new Szenario(Route.MONTREAL_TRUDEAU);

        Flughoehe flughoehe = szenario.getCurrentFlughoehe();
        assertNotNull(flughoehe);
        assertTrue(flughoehe.getHoehe() > 0);
    }

    @Test
    void testSetHoeheSzenarioTypeWithValidType() throws IOException {
        Szenario szenario = new Szenario(Route.MONTREAL_TRUDEAU);
        szenario.setHoeheSzenarioType("GREEN");

        assertEquals(HoeheSzenarioTypes.GREEN, szenario.getHoeheSzenarioType());
    }

    @Test
    void testGetCurrentFieldWithValidPosition() throws IOException {
        Szenario szenario = new Szenario(Route.HANEDA);

        ReiseFeld field = szenario.getCurrentField(0);
        assertNotNull(field);
        assertFalse(field.istFlughafen());
    }

    @Test
    void testGetCurrentFieldWithInvalidPosition() throws IOException {
        Szenario szenario = new Szenario(Route.HANEDA);

        assertThrows(IllegalArgumentException.class, () -> szenario.getCurrentField(999));
    }

}
