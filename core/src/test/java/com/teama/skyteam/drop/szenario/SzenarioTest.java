package com.teama.skyteam.drop.szenario;

import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;


class SzenarioTest {

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
