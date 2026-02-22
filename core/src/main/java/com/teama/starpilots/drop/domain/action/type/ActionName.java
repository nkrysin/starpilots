package com.teama.starpilots.drop.domain.action.type;

public enum ActionName {
    // Pilot Controls
    PILOT_RADIO,
    PILOT_TILT,
    PILOT_SPEED,
    PILOT_LANDING_GEAR_1_2,
    PILOT_LANDING_GEAR_3_4,
    PILOT_LANDING_GEAR_5_6,
    PILOT_BRAKES_2,
    PILOT_BRAKES_4,
    PILOT_BRAKES_6,
    PILOT_COFFEE_1,
    PILOT_COFFEE_2,
    PILOT_COFFEE_3,

    // Copilot Controls
    COPILOT_TILT,
    COPILOT_SPEED,
    COPILOT_RADIO_1,
    COPILOT_RADIO_2,
    COPILOT_FLAPS_1_2,
    COPILOT_FLAPS_2_3,
    COPILOT_FLAPS_4_5,
    COPILOT_FLAPS_5_6,
    COPILOT_COFFEE_1,
    COPILOT_COFFEE_2,
    COPILOT_COFFEE_3,

    // Gemeinsame
    USE_COFFEE_1,
    USE_COFFEE_2,
    USE_COFFEE_3,
    REROLL;

    public static ActionName useCoffee(int i) {
        return switch (i) {
            case 1 -> USE_COFFEE_1;
            case 2 -> USE_COFFEE_2;
            case 3 -> USE_COFFEE_3;
            default -> throw new IllegalArgumentException("Ungültiger Kaffee-Index: " + i);
        };
    }
    }
