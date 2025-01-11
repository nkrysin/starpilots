package com.teama.skyteam.drop.flugzeug;

import com.teama.skyteam.drop.tokens.Wuerfel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlugzeugTest {

    private Flugzeug flugzeug;
    private Wuerfel mockWuerfel1;
    private Wuerfel mockWuerfel2;

    @BeforeEach
    void setUp() {
        flugzeug = new Flugzeug();
        mockWuerfel1 = mock(Wuerfel.class);
        mockWuerfel2 = mock(Wuerfel.class);
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals(0, flugzeug.getSpeedAktuell());
        assertEquals(Speed.FOUR, flugzeug.getSpeedUntereGrenze());
        assertEquals(Speed.EIGHT, flugzeug.getSpeedObereGrenze());
        assertEquals(0, flugzeug.getNeigung());
        assertEquals(Bremsen.NONE, flugzeug.getBremsenZustand());
        assertEquals(Klappen.NONE, flugzeug.getKlappenZustand());

        assertNotNull(flugzeug.getFahrgestellZustand());
        assertEquals(0, flugzeug.getFahrgestellZustand().getAkrivierteFahrgestelle());
    }

    @Test
    public void testFlugzeugNeigenMit2GleichenWerten() {
        when(mockWuerfel1.getWert()).thenReturn(3);
        when(mockWuerfel2.getWert()).thenReturn(3);

        flugzeug.neigen(flugzeug.getNeigung(), new Wuerfel[]{mockWuerfel1, mockWuerfel2});
        assertEquals(0, flugzeug.getNeigung());
    }


    @Test
    public void testFlugzeugNeigenMit2VerschiedenWertenCopilotGroesser() {
        when(mockWuerfel1.getWert()).thenReturn(4);
        when(mockWuerfel2.getWert()).thenReturn(5);

        flugzeug.neigen(flugzeug.getNeigung(), new Wuerfel[]{mockWuerfel1, mockWuerfel2});
        assertEquals(-1, flugzeug.getNeigung());
    }

    @Test
    public void testFlugzeugNeigenMit2VerschiedenWertenPilotGroesser() {
        when(mockWuerfel1.getWert()).thenReturn(6);
        when(mockWuerfel2.getWert()).thenReturn(4);

        flugzeug.neigen(flugzeug.getNeigung(), new Wuerfel[]{mockWuerfel1, mockWuerfel2});
        assertEquals(2, flugzeug.getNeigung());
    }

    @Test
    public void testFlugzeugBremsenLevel1() {
        flugzeug.setBremsenZustand(Bremsen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(2);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Bremsen.LEVEL1, flugzeug.getBremsenZustand());
    }

    @Test
    public void testFlugzeugBremsenLevel2() {
        flugzeug.setBremsenZustand(Bremsen.LEVEL1);
        when(mockWuerfel1.getWert()).thenReturn(4);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Bremsen.LEVEL2, flugzeug.getBremsenZustand());
    }


    @Test
    public void testFlugzeugBremsenLevel3() {
        flugzeug.setBremsenZustand(Bremsen.LEVEL2);
        when(mockWuerfel1.getWert()).thenReturn(6);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Bremsen.LEVEL3, flugzeug.getBremsenZustand());
    }


    @Test
    public void testFlugzeugBremsenNichtLevel3() {
        flugzeug.setBremsenZustand(Bremsen.LEVEL1);
        when(mockWuerfel1.getWert()).thenReturn(6);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertFalse(result);
        assertEquals(Bremsen.LEVEL1, flugzeug.getBremsenZustand());
    }


    @Test
    public void testFlugzeugBremsenNichtLevel2() {
        flugzeug.setBremsenZustand(Bremsen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(4);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertFalse(result);
        assertEquals(Bremsen.NONE, flugzeug.getBremsenZustand());
    }


    @Test
    public void testFlugzeugBremsenNichtLevel1() {
        flugzeug.setBremsenZustand(Bremsen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(1);
        boolean result = flugzeug.bremsen(mockWuerfel1);

        assertFalse(result);
        assertEquals(Bremsen.NONE, flugzeug.getBremsenZustand());
    }


    @Test
    public void testFlugzeugKlappenOeffnenLevel1MitWert1() {
        flugzeug.setKlappenZustand(Klappen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(1);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL1, flugzeug.getKlappenZustand());
        assertEquals(Speed.NINE, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenLevel1MitWert2() {
        flugzeug.setKlappenZustand(Klappen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(2);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL1, flugzeug.getKlappenZustand());
        assertEquals(Speed.NINE, flugzeug.getSpeedObereGrenze());
    }


    @Test
    public void testFlugzeugKlappenOeffnenLevel2MitWert2() {
        flugzeug.setKlappenZustand(Klappen.LEVEL1);
        when(mockWuerfel1.getWert()).thenReturn(2);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL2, flugzeug.getKlappenZustand());
        assertEquals(Speed.TEN, flugzeug.getSpeedObereGrenze());
    }


    @Test
    public void testFlugzeugKlappenOeffnenLevel2MitWert3() {
        flugzeug.setKlappenZustand(Klappen.LEVEL1);
        when(mockWuerfel1.getWert()).thenReturn(3);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL2, flugzeug.getKlappenZustand());
        assertEquals(Speed.TEN, flugzeug.getSpeedObereGrenze());
    }


    @Test
    public void testFlugzeugKlappenOeffnenLevel3MitWert4() {
        flugzeug.setKlappenZustand(Klappen.LEVEL2);
        when(mockWuerfel1.getWert()).thenReturn(4);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL3, flugzeug.getKlappenZustand());
        assertEquals(Speed.ELEVEN, flugzeug.getSpeedObereGrenze());
    }


    @Test
    public void testFlugzeugKlappenOeffnenLevel3MitWert5() {
        flugzeug.setKlappenZustand(Klappen.LEVEL2);
        when(mockWuerfel1.getWert()).thenReturn(5);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL3, flugzeug.getKlappenZustand());
        assertEquals(Speed.ELEVEN, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenLevel4MitWert5() {
        flugzeug.setKlappenZustand(Klappen.LEVEL3);
        when(mockWuerfel1.getWert()).thenReturn(5);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL4, flugzeug.getKlappenZustand());
        assertEquals(Speed.TWELVE, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenLevel4MitWert6() {
        flugzeug.setKlappenZustand(Klappen.LEVEL3);
        when(mockWuerfel1.getWert()).thenReturn(6);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertTrue(result);
        assertEquals(Klappen.LEVEL4, flugzeug.getKlappenZustand());
        assertEquals(Speed.TWELVE, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenNichtLevel1() {
        flugzeug.setKlappenZustand(Klappen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(3);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertFalse(result);
        assertNotEquals(Klappen.LEVEL1, flugzeug.getKlappenZustand());
        assertNotEquals(Speed.NINE, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenNichtLevel2() {
        flugzeug.setKlappenZustand(Klappen.NONE);
        when(mockWuerfel1.getWert()).thenReturn(4);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertFalse(result);
        assertNotEquals(Klappen.LEVEL2, flugzeug.getKlappenZustand());
        assertNotEquals(Speed.TEN, flugzeug.getSpeedObereGrenze());
    }

    @Test
    public void testFlugzeugKlappenOeffnenNichtLevel3() {
        flugzeug.setKlappenZustand(Klappen.LEVEL2);
        when(mockWuerfel1.getWert()).thenReturn(3);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertFalse(result);
        assertNotEquals(Klappen.LEVEL3, flugzeug.getKlappenZustand());
        assertNotEquals(Speed.ELEVEN, flugzeug.getSpeedObereGrenze());

    }

    @Test
    public void testFlugzeugKlappenOeffnenNichtLevel4() {
        flugzeug.setKlappenZustand(Klappen.LEVEL2);
        when(mockWuerfel1.getWert()).thenReturn(6);
        boolean result = flugzeug.klappenOeffnen(mockWuerfel1);

        assertFalse(result);
        assertNotEquals(Klappen.LEVEL4, flugzeug.getKlappenZustand());
        assertNotEquals(Speed.TWELVE, flugzeug.getSpeedObereGrenze());
    }

    @Test
    void testFahrgestellAusfahrenWert() {
        Wuerfel mockWuerfel1 = mock(Wuerfel.class);
        when(mockWuerfel1.getWert()).thenReturn(1);

        boolean result = flugzeug.fahrgestellAusfahren(mockWuerfel1);
        assertTrue(result);
        assertEquals(Speed.FIVE, flugzeug.getSpeedUntereGrenze());

        Wuerfel mockWuerfel3 = mock(Wuerfel.class);
        when(mockWuerfel3.getWert()).thenReturn(3);

        result = flugzeug.fahrgestellAusfahren(mockWuerfel3);

        assertTrue(result);
        assertEquals(Speed.SIX, flugzeug.getSpeedUntereGrenze());

        Wuerfel mockWuerfel5 = mock(Wuerfel.class);
        when(mockWuerfel5.getWert()).thenReturn(5);

        result = flugzeug.fahrgestellAusfahren(mockWuerfel5);

        assertTrue(result);
        assertEquals(Speed.SEVEN, flugzeug.getSpeedUntereGrenze());
    }
}
