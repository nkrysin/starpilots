package com.teama.skyteam.drop.spiel.action.type;

public enum ActionName {
    // Pilot Controls
    PILOT_RADIO,
    PILOT_NEIGUNG,
    PILOT_SPEED,
    PILOT_FAHRGESTELL_1_2,
    PILOT_FAHRGESTELL_3_4,
    PILOT_FAHRGESTELL_5_6,
    PILOT_BREMSEN_2,
    PILOT_BREMSEN_4,
    PILOT_BREMSEN_6,
    PILOT_KAFFEE_1,
    PILOT_KAFFEE_2,
    PILOT_KAFFEE_3,

    // Copilot Controls
    COPILOT_NEIGUNG,
    COPILOT_SPEED,
    COPILOT_RADIO_1,
    COPILOT_RADIO_2,
    COPILOT_KLAPPEN_1_2,
    COPILOT_KLAPPEN_2_3,
    COPILOT_KLAPPEN_4_5,
    COPILOT_KLAPPEN_5_6,
    COPILOT_KAFFEE_1,
    COPILOT_KAFFEE_2,
    COPILOT_KAFFEE_3,

    // Gemeinsame
    USE_KAFFEE_1,
    USE_KAFFEE_2,
    USE_KAFFEE_3,
    NEUWURF;

    public static ActionName useKaffee(int i) {
        return switch (i) {
            case 1 -> USE_KAFFEE_1;
            case 2 -> USE_KAFFEE_2;
            case 3 -> USE_KAFFEE_3;
            default -> throw new IllegalArgumentException("Ungültiger Kaffee-Index: " + i);
        };
    }
    }
