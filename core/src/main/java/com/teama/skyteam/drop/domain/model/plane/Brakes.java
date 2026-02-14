package com.teama.skyteam.drop.domain.model.plane;

public enum Brakes {
    NONE(1),
    LEVEL1(2),
    LEVEL2(4),
    LEVEL3(6);

    private final int value;

    Brakes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

