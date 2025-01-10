package com.teama.skyteam.drop.flugzeug;

public enum Klappen {
    NONE(8),
    LEVEL1(9),
    LEVEL2(10),
    LEVEL3(11),
    LEVEL4(12);

    private final int value;

    Klappen
        (int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
