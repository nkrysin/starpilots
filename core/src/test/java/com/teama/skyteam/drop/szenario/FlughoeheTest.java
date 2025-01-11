package com.teama.skyteam.drop.szenario;

import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlughoeheTest {


    @Test
    void testCreateGreenSzenario() {
        List<Flughoehe> greenSzenario = Flughoehe.createHoeheSzenario("GREEN");

        assertNotNull(greenSzenario);
        assertFalse(greenSzenario.isEmpty());

        for (Flughoehe flughoehe : greenSzenario) {
            assertTrue(flughoehe.getHoehe() >= 0);
        }

        assertTrue(greenSzenario.get(4).hatErneuWerfen());
        assertTrue(greenSzenario.get(0).hatErneuWerfen());

    }

    @Test
    void testCreateRedSzenario() {
        List<Flughoehe> redSzenario = Flughoehe.createHoeheSzenario("RED");

        assertNotNull(redSzenario);
        assertFalse(redSzenario.isEmpty());

        for (Flughoehe flughoehe : redSzenario) {
            assertTrue(flughoehe.getHoehe() >= 0);
        }

        assertTrue(redSzenario.getFirst().hatErneuWerfen());
    }

    @Test
    void testInvalidTypeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Flughoehe.createHoeheSzenario("INVALID_TYPE");
        });

        String expectedMessage = "Ungültiger Typ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), expectedMessage);
    }
}

