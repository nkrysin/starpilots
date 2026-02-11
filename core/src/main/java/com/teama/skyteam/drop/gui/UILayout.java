package com.teama.skyteam.drop.gui;

public class UILayout {

    private UILayout() {}
    // Koordinaten der Schalter und Indikatoren. Diese gelten nur für FUllHD-Bildschirme
    public static final int[][] FAHRGESTELL_KOORDINATEN = {
        {705, 545},
        {705, 410},
        {705, 275}
    };

    public static final int[][] BREMSEN_KOORDINATEN = {
        {867, 242},
        {960, 242},
        {1051, 242}
    };

    public static final int[][] KLAPPEN_KOORDINATEN = {
        {1213, 545},
        {1213, 410},
        {1213, 275},
        {1213, 139}
    };

    public static final int[][] BREMSEN_INDICATOR_KOORDINATEN = {
        {823, 403},
        {866, 373},
        {970, 355},
        {1067, 394}
    };

    public static final int[][] FAHRGESTELL_INDICATOR_KOORDINATEN = {
        {822, 570},
        {865, 539},
        {916, 522},
        {970, 520}
    };

    public static final int[][] KLAPPEN_INDICATOR_KOORDINATEN = {
        {1020, 532},
        {1067, 560},
        {1103, 599},
        {1127, 644},
        {1137, 699}
    };

    public static final int SCHALTER_STEP = 30;

    public static final float BREMSEN_INDICATOR_ANGLE = -40f;
    public static final float FAHRGESTELL_INDICATOR_ANGLE = -40f;
    public static final float KLAPPEN_INDICATOR_ANGLE = 25f;

    public static final float MOVE_DURATION = 0.3f;

    public static final float BREMSEN_ROTATION_CASE_2 = -25f;
    public static final float BREMSEN_ROTATION_CASE_4 = 10f;
    public static final float BREMSEN_ROTATION_CASE_6 = 40f;

    public static final float FAHRGESTELL_ROTATION_CASE_1 = -25f;
    public static final float FAHRGESTELL_ROTATION_CASE_2 = -10f;
    public static final float FAHRGESTELL_ROTATION_CASE_3 = 10f;

    public static final float KLAPPEN_ROTATION_CASE_9 = 45f;
    public static final float KLAPPEN_ROTATION_CASE_10 = 60f;
    public static final float KLAPPEN_ROTATION_CASE_11 = 75f;
    public static final float KLAPPEN_ROTATION_CASE_12 = 90f;

    public static final int MOVE_STEP = 96; // Die Schrittgröße für die Bewegung der Ansicht. Gilt für FUllHD-Bildschirme
    public static final float[][] PLANE_POSITIONS = {  // Positionen der Flugzeuge auf der Karte.  Das gilt nur für FUllHD-Bildschirme
        {792, 886},
        {834, 886},
        {876, 886}
    };

    public static final int HOEHE_FULL_HD_X_SIZE = 989;
    public static final int HOEHE_FULL_HD_Y_SIZE = 841;


    // Koordinaten von Wuerfeln. Diese gelten nur für FUllHD-Bildschirme
    public static final int[][] PILOT_WUERFEL_COORDINATES = {{411, 553}, {491, 553}, {411, 473}, {491, 473}};
    public static final int[][] COPILOT_WUERFEL_COORDINATES = {{1367, 553}, {1448, 553}, {1367, 473}, {1448, 473}};
    public static final int[] RUDER_COORDINATES = {868, 630};
    public static final int[][] PILOT_ACTOR_COORDINATES = {{679, 791}, {773, 777}, {838, 475}, {679, 598}, {679, 464}, {679, 327}, {842, 293}, {933, 293}, {1025, 293}};
    public static final int[][] COPILOT_ACTOR_COORDINATES = {{1091, 777}, {1027, 475}, {1185, 791}, {1185, 883}, {1185, 598}, {1185, 464}, {1185, 327}, {1185, 191}};
    public static final int[] WUERFEL_SIZE = {55,55};
    public static final int[][] KAFFEE_COORDINATES = {{842, 142}, {933, 142}, {1025, 142}};
    public static final int[][] KAFFEE_CONTROL_COORDINATES = {{711, 201}, {711, 143}, {772, 143}};
    public static final int[] KAFFEE_BUTTON_SIZE = {39,39};
    public static final int[] PILOT_HIDING_SCREEN_COORDINATES = {330, 390};
    public static final int[] COPILOT_HIDING_SCREEN_COORDINATES = {1290, 390};
    public static final int[] NEUWURF_BUTTON_SIZE = {60,60};
    public static final int[] NEUWURF_BUTTON_COORDINATES = {681,873};

    // Koordinaten von Szenario
    public static final int[] SZENARIO_COORDINATES = {758,845};
    public static final int[] SZENARIO_PLANE_SIZE = {39,39};
    public static final int SZENARIO_PLANE_Y_COORDINATE = 886;
    public static final float SZENARIO_PLANE_Y_OFFSET = 0f;
}
