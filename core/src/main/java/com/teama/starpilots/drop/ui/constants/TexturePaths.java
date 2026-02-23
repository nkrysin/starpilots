package com.teama.starpilots.drop.ui.constants;

public final class TexturePaths {
    private TexturePaths() {}

    // Base folders
    private static final String ASSETS_PATH = "images";
    private static final String ASSETS_GAME_PATH = ASSETS_PATH + "/game";
    private static final String ASSETS_ALTITUDE_PATH = ASSETS_PATH + "/altitude";
    public static final String ASSETS_CAMPAIGN_PATH = ASSETS_PATH + "/campaigns/";

    // Formats
    public static final String IMG_FORMAT = ".png";

    // Game folders
    private static final String MAIN = ASSETS_GAME_PATH + "/main";
    private static final String DICE = ASSETS_GAME_PATH + "/dices";
    private static final String TOKENS = ASSETS_GAME_PATH + "/tokens";
    private static final String MENU = ASSETS_GAME_PATH + "/menu";
    private static final String MARKERS = ASSETS_GAME_PATH + "/markers";

    // Main
    public static final String RUDDER_PATH = MAIN + "/axis.png";
    public static final String AVAILABLE_FIELD_PATH = MAIN + "/available.png";
    public static final String WITHOUT_HIGHLIGHT_PATH = MAIN + "/empty.png";
    public static final String STARSHIP_SWITCH_PATH = MAIN + "/starship_switch.png";
    public static final String BACKGROUND_PATH = MAIN + "/pause_background.png";
    public static final String TABLE_PATH = MAIN + "/table.png";
    public static final String BOARD_PATH = MAIN + "/board.png";
    public static final String WIN_PATH = MAIN + "/dog-happy.png";
    public static final String LOOSE_PATH = MAIN + "/frog-sad.png";

    // Dice
    public static final String DICE_PATH_PILOT = DICE + "/blue/blue_";
    public static final String DICE_PATH_COPILOT = DICE + "/orange/orange_";
    public static final String DICE_PATH_SELECTED = "_selected" + IMG_FORMAT;
    public static final String DICE_COVER_PATH = DICE + "/dice_cover.png";

    // Tokens
    public static final String COFFEE_PATH = TOKENS + "/coffee.png";
    public static final String COFFEE_AVAILABLE_PATH = TOKENS + "/coffee_available.png";
    public static final String COFFEE_SELECTED_PATH = TOKENS + "/coffee_selected.png";

    public static final String REROLL_PATH = TOKENS + "/reroll.png";
    public static final String REROLL_AVAILABLE_PATH = TOKENS + "/reroll_available.png";
    public static final String REROLL_SELECTED_PATH = TOKENS + "/reroll_selected.png";

    public static final String ASTEROID_PATH = TOKENS + "/asteroid.png";

    // Markers
    public static final String RED_MARKER = MARKERS + "/red_marker.png";
    public static final String PURPLE_MARKER = MARKERS + "/purple_marker.png";
    public static final String PINK_MARKER = MARKERS + "/pink_marker.png";

    // Altitude
    public static final String ALTITUDE_RED_PATH = ASSETS_ALTITUDE_PATH + "/RED" + IMG_FORMAT;
    public static final String ALTITUDE_GREEN_PATH = ASSETS_ALTITUDE_PATH + "/GREEN" + IMG_FORMAT;

    // Menu
    public static final String LOGO_PATH = MENU + "/logo.png";
    public static final String BACKGROUND_MENU_PATH = MENU + "/bg_menu1.png";
    public static final String BACKGROUND_CAMPAIGN_SELECTION_PATH = MENU + "/bg_menu2.png";
    public static final String START_SCREEN_LOGO = MENU + "/start_screen_logo.png";

    // Skin
    public static final String SKIN_PATH = "skin/flat-earth-ui.json";
}
