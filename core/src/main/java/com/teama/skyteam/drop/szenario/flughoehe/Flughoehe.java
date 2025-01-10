package com.teama.skyteam.drop.szenario.flughoehe;

import java.util.ArrayList;
import java.util.List;

public class Flughoehe {
    private int hoehe;
    private boolean hatErneuWerfen;

    private Flughoehe(int hoehe, boolean hatErneuWerfen) {
        this.hoehe = hoehe;
        this.hatErneuWerfen = hatErneuWerfen;
    }

    public static List<Flughoehe> createHoeheSzenario(String type) {
        switch (type.toUpperCase()) {
            case "GREEN":
                return createGreenSzenario();
            case "RED":
                return createRedSzenario();
            default:
                throw new IllegalArgumentException("Ungültiger Typ: " + type + ". Gültige Typen sind ‚GREEN‘ oder ‚RED‘");
        }
    }

    private static List<Flughoehe> createGreenSzenario() {
        List<Flughoehe> greenSzenario = new ArrayList<>();
        for (Green green : Green.values()) {
            greenSzenario.add(new Flughoehe(green.getHoehe(), green.hatErneuWerfen()));
        }
        return greenSzenario;
    }

    private static List<Flughoehe> createRedSzenario() {
        List<Flughoehe> redSzenario = new ArrayList<>();
        for (Red red : Red.values()) {
            redSzenario.add(new Flughoehe(red.getHoehe(), red.hatErneuWerfen()));
        }
        return redSzenario;
    }

    public int getHoehe() {
        return hoehe;
    }

    public boolean hatErneuWerfen() {
        return hatErneuWerfen;
    }
}
