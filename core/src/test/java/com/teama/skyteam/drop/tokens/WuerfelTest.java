package com.teama.skyteam.drop.tokens;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class WuerfelTest {

    @Test
    void getWert() {
        for (int i = 0; i < 1000; i++) {
            Wuerfel wuerfel = new Wuerfel();
            int value = wuerfel.getWert();
            assertTrue(value >= 1 && value <= 6);
        }
    }
}
