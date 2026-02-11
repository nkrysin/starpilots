package com.teama.skyteam.drop.gui;

public final class TexturePaths {
    private TexturePaths() {}

    // Base folders
    private static final String ASSETS_PATH = "images";
    private static final String ASSETS_SPIEL_PATH = ASSETS_PATH + "/spiel";
    private static final String ASSETS_HOEHE_PATH = ASSETS_PATH + "/hoehe";
    public static final String ASSETS_SZENARIO_PATH = ASSETS_PATH + "/szenario/";

    // Formats
    public static final String IMG_FORMAT = ".png";

    // Spiel folders
    private static final String MAIN = ASSETS_SPIEL_PATH + "/main";
    private static final String DICES = ASSETS_SPIEL_PATH + "/dices";
    private static final String TOKENS = ASSETS_SPIEL_PATH + "/tokens";
    private static final String MENU = ASSETS_SPIEL_PATH + "/menu";
    private static final String MARKERS = ASSETS_SPIEL_PATH + "/markers";

    // Main
    public static final String RUDER_PATH = MAIN + "/axis.png";
    public static final String AVAILABLE_FIELD_PATH = MAIN + "/available.png";
    public static final String WITHOUT_HIGHLIGHT_PATH = MAIN + "/empty.png";
    public static final String PLANE_SWITCH_PATH = MAIN + "/plane_switch.png";
    public static final String BACKGROUND_PATH = MAIN + "/pause_background.png";
    public static final String TABLE_PATH = MAIN + "/table_w_arrows.png";
    public static final String BOARD_PATH = MAIN + "/board.png";
    public static final String WIN_PATH = MAIN + "/dog-happy.png";
    public static final String LOOSE_PATH = MAIN + "/frog-sad.png";

    // Dices
    public static final String WUERFEL_PATH_PILOT = DICES + "/blue/blue_";
    public static final String WUERFEL_PATH_COPILOT = DICES + "/orange/orange_";
    public static final String WUERFEL_PATH_SELECTED = "_selected" + IMG_FORMAT;
    public static final String DICE_COVER_PATH = DICES + "/dice_cover.png";

    // Tokens
    public static final String KAFFEE_PATH = TOKENS + "/kaffee.png";
    public static final String KAFFEE_AVAILABLE_PATH = TOKENS + "/kaffee_available.png";
    public static final String KAFFEE_SELECTED_PATH = TOKENS + "/kaffee_selected.png";

    public static final String NEUWURF_PATH = TOKENS + "/neuwurf.png";
    public static final String NEUWURF_AVAILABLE_PATH = TOKENS + "/neuwurf_available.png";
    public static final String NEUWURF_SELECTED_PATH = TOKENS + "/neuwurf_selected.png";

    public static final String PLANE_PATH = TOKENS + "/plane.png";

    // Markers
    public static final String RED_MARKER = MARKERS + "/red_marker.png";
    public static final String BLUE_MARKER = MARKERS + "/blue_marker.png";
    public static final String ORANGE_MARKER = MARKERS + "/orange_marker.png";

    // Hoehe (проверь реальные имена файлов)
    public static final String HOEHE_RED_PATH = ASSETS_HOEHE_PATH + "/RED" + IMG_FORMAT;
    public static final String HOEHE_GREEN_PATH = ASSETS_HOEHE_PATH + "/GREEN" + IMG_FORMAT;

    // Menu
    public static final String CREDITS_PATH = MENU + "/credits.png";
    public static final String LOGO_PATH = MENU + "/logo.png";
    public static final String BACKGROUND_MENU_PATH = MENU + "/bg_menu1.png";
    public static final String BACKGROUND_SZENARIO_SELECTION_PATH = MENU + "/bg_menu2.png";
    public static final String TEAM_A_LOGO = MENU + "/team_a.png";
    public static final String START_SCREEN_LOGO = MENU + "/start_screen_logo.png";

    // Skin
    public static final String SKIN_PATH = "skin/flat-earth-ui.json";
}
