package com.teama.skyteam.drop.domain.model.flugzeug;

public enum Bremsen {
    NONE(1),
    LEVEL1(2),
    LEVEL2(4),
    LEVEL3(6);

    private final int value;

    Bremsen(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

