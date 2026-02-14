package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.application.gameservice.GameService;
import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.domain.action.factory.ActionFactory;
import com.teama.skyteam.drop.domain.action.validator.ActionValidator;
import com.teama.skyteam.drop.domain.action.validator.StandardActionValidator;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.gamestate.StandardGameStateManager;
import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.application.logic.fields.StandardFieldManager;
import com.teama.skyteam.drop.domain.rules.GameRules;
import com.teama.skyteam.drop.domain.rules.StandartGameRules;
import com.teama.skyteam.drop.domain.model.player.Pilot;

import java.util.ArrayList;

/**
 * Die Klasse GameControllerGUI steuert den Spielfluss und die Interaktion mit der Benutzeroberfläche.
 * Sie verwendet die Zustandsmaschine, um den Spielverlauf zu organisieren und zwischen verschiedenen Spielphasen zu wechseln.
 */
public class GameControllerGUI {

    private final Game game;
    private final GameService gameService;
    private final CampaignAltitudeManager campaignAltitudeManager;
    private final GameGUI gameGUI;
    boolean nowMustBeFirst = true;
    boolean nowMustBeSecond = false;
    private State state = State.CHECK_ENDGAME;
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean isTiltCompleted = false;
    private boolean isRouteFieldUpdated = false;
    private boolean lastRound = false;
    private boolean endgame = false;
    private final GameStateManager gameStateManager;
    private final FieldManager fieldManager;

    public GameControllerGUI(Game game, CampaignAltitudeManager campaignAltitudeManager, Stage stage, Skin skin) {
        this.game = game;
        this.campaignAltitudeManager = campaignAltitudeManager;
        this.fieldManager = new StandardFieldManager();

        this.gameStateManager = new StandardGameStateManager(game);
        ActionValidator validator = new StandardActionValidator();
        ActionFactory actionFactory = new ActionFactory(game, gameStateManager);
        GameRules gameRules = new StandartGameRules();

        gameService = new GameService(game, gameRules, validator, actionFactory, gameStateManager);

        this.gameGUI = new GameGUI(game, stage, skin, campaignAltitudeManager, gameService, gameStateManager, fieldManager);


        // Setzt die Callback-Funktion für Spieleraktionen.
        this.gameGUI.setPlayerActionCallback(new PlayerActionCallback() {
            @Override
            public void onPlayerActionCompleted(Player player) {
                gameGUI.clearPlayerListeners(player);
                playStep();
            }
        });
        playStep();
    }


    /**
     * Aufzählung von Zuständen für die Zustandsmaschine des Spiels.
     */
    private enum State {
        CHECK_ENDGAME,
        PREPARE_ROUND,
        PLAY_TURN_FIRST,
        CHECK_TILT,
        CHECK_ROUTE_FIELD,
        PLAY_TURN_SECOND,
        CHECK_TILT_AGAIN,
        CHECK_ROUTE_FIELD_AGAIN,
        ROUND_FINISH,
        FINAL_CHECK,
        ENDGAME
    }

    /**
     * Bereitet eine neue Runde vor, indem die Player und ihre Würfel initialisiert werden.
     * Wird auch berechnet, wer einen ersten Zug in der Runde macht
     */
    private void prepareRound() {
        isTiltCompleted = false;
        isRouteFieldUpdated = false;

        gameStateManager.addRerollTokens();
        boolean isEvenRound = (game.getRound() + 1) % 2 == 0;
        firstPlayer = isEvenRound ? game.getPilot() : game.getCopilot();
        secondPlayer = isEvenRound ? game.getCopilot() : game.getPilot();

        gameStateManager.setNewDices(firstPlayer);
        gameStateManager.setNewDices(secondPlayer);

        gameGUI.createDiceForPlayer(firstPlayer);
        gameGUI.createDiceForPlayer(secondPlayer);
    }

    /**
     * Beendet die aktuelle Runde, aktualisiert die Spielfelder und prüft auf FailCondition.
     */
    private void finishRound() {
        ArrayList<Field> oneUseFields = (ArrayList<Field>) fieldManager.getOneUseFields(game);
        fieldManager.removeDiceFromOneUseFields(oneUseFields);
        fieldManager.clearManyUseFields(game);
        gameGUI.clearManyUseFields();
        campaignAltitudeManager.updateAltitude();

        game.setRound(game.getRound() + 1);
        gameStateManager.reduceAltitude();
        game.setEndCondition(gameService.checkFailCondition());
        gameGUI.removeAllHideImages();
    }

    /**
     * Die Methode playStep steuert den Spielfluss basierend auf einer Zustandsmaschine.
     * Sie wechselt zwischen den verschiedenen Zuständen des Spiels und verwaltet die Aktionen der Player,
     * die Prüfung von Spielfeldern und das Ende des Spiels.
     *
     * Der Ablauf wird in mehreren Zuständen organisiert:
     *
     * 1. CHECK_END: Prüft, ob eine FailCondition erreicht wurde.
     *    Wenn ja, endet das Game. Ansonsten wird zur Vorbereitung der nächsten Runde gewechselt.
     *
     * 2. PREPARE_ROUND: Initialisiert die Runde, setzt Würfel und Player für die aktuelle Runde.
     *
     * 3. PLAY_TURN_FIRST: Führt die Aktionen des ersten Spielers aus.
     *    Der Player kann Würfel auf Feldern platzieren, spezielle Aktionen wie Kaffee- oder NeuWurf-Tokens ausführen.
     *    Nach dem Zug des ersten Spielers wird die Neigung geprüft.
     *
     * 4. CHECK_NEIGUNG: Überprüft und aktualisiert die Neigung des Flugzeugs, falls erforderlich.
     *    Anschließend wird der Zustand des Spielfelds geprüft.
     *
     * 5. CHECK_REISEFELD: Aktualisiert den Zustand des aktuellen Reisefelds basierend auf der Geschwindigkeit.
     *    Danach wird der Zug des zweiten Spielers eingeleitet.
     *
     * 6. PLAY_TURN_SECOND: Führt die Aktionen des zweiten Spielers aus.
     *    Analog zum ersten Player können Würfel platziert und spezielle Aktionen ausgeführt werden.
     *    Nach dem Zug des zweiten Spielers wird die Neigung erneut geprüft.
     *
     * 7. CHECK_NEIGUNG_AGAIN: Prüft die Neigung erneut nach dem Zug des zweiten Spielers.
     *    Danach wird der Zustand des Spielfelds erneut überprüft.
     *
     * 8. CHECK_REISEFELD_AGAIN: Führt eine erneute Prüfung des Spielfelds durch.
     *    Falls noch Würfel übrig sind, wird der Zug des ersten Spielers wiederholt.
     *    Andernfalls wird die Runde beendet.
     *
     * 9. ROUND_FINISH: Beendet die Runde, bereinigt Spielfelder und prüft erneut auf FailConditionen.
     *    Wenn die letzte Runde erreicht wurde, wird zur finalen Überprüfung gewechselt.
     *
     * 10. FINAL_CHECK: Prüft, ob die Gewinnbedingungen erfüllt sind.
     *     Zeigt ein finales Fenster an, das entweder den Sieg oder die Niederlage signalisiert.
     *
     * 11. END: Zeigt das Endfenster an und beendet das Game.
     *
     * Die Methode verwendet eine Schleife, um den aktuellen Zustand kontinuierlich zu überprüfen und in den nächsten zu wechseln.
     * Die Zustände definieren dabei klar die logische Abfolge der Spielaktionen.
     */
    public void playStep() {
        if (endgame) {
            return;
        }
        while (!endgame) {
            switch (state) {
                case CHECK_ENDGAME:
                    if (gameService.checkFailCondition()) {
                        endgame = true;
                        state = State.ENDGAME;
                        break;
                    } else {
                        if (!lastRound && gameService.checkFinalRound()) {
                            lastRound = true;
                        }
                        state = State.PREPARE_ROUND;
                    }
                    break;

                case PREPARE_ROUND:

                    prepareRound();
                    state = State.PLAY_TURN_FIRST;
                    break;

                case PLAY_TURN_FIRST:
                    if (firstPlayer.getAllDiceCount() > 0 && !gameService.checkFailCondition() && nowMustBeFirst) {
                        gameGUI.removeAllHideImages();
                        gameGUI.hideDice(firstPlayer);
                        gameGUI.addDiceListenersForSpieler(firstPlayer);
                        gameGUI.addFieldListenersForPlacement(firstPlayer);
                        gameGUI.checkFieldSituation(firstPlayer);
                        gameGUI.setCoffeeGroupsActive(firstPlayer);

                        if (firstPlayer instanceof Pilot) {
                            gameGUI.showCoffee(true, false);
                        } else {
                            gameGUI.showCoffee(false, true);
                        }
                        gameGUI.showReroll(true);

                        gameGUI.useCoffee(firstPlayer);

                        gameGUI.useReroll(firstPlayer);

                        nowMustBeFirst = false;
                        nowMustBeSecond = true;
                        return;
                    } else {
                        state = State.CHECK_TILT;
                    }
                    break;

                case CHECK_TILT:
                    if (!isTiltCompleted && gameStateManager.updateTilt().isSuccess()) {
                        isTiltCompleted = true;
                    }

                    state = State.CHECK_ROUTE_FIELD;
                    break;

                case CHECK_ROUTE_FIELD:
                    if (!isRouteFieldUpdated && gameStateManager.updateTravelFieldState().isSuccess()) {
                        isRouteFieldUpdated = true;
                    }

                    state = State.PLAY_TURN_SECOND;
                    break;

                case PLAY_TURN_SECOND:
                    if (secondPlayer.getAllDiceCount() > 0 && !gameService.checkFailCondition() && nowMustBeSecond) {
                        gameGUI.removeAllHideImages();
                        gameGUI.hideDice(secondPlayer);
                        gameGUI.addDiceListenersForSpieler(secondPlayer);
                        gameGUI.addFieldListenersForPlacement(secondPlayer);
                        gameGUI.checkFieldSituation(secondPlayer);
                        gameGUI.setCoffeeGroupsActive(secondPlayer);

                        if (secondPlayer instanceof Pilot) {
                            gameGUI.showCoffee(true, false);
                        } else {
                            gameGUI.showCoffee(false, true);
                        }
                        gameGUI.showReroll(true);
                        gameGUI.useReroll(secondPlayer);
                        gameGUI.useCoffee(secondPlayer);

                        nowMustBeFirst = true;
                        nowMustBeSecond = false;
                        return;
                    } else {
                        state = State.CHECK_TILT_AGAIN;
                    }
                    break;

                case CHECK_TILT_AGAIN:
                    if (!isTiltCompleted && gameStateManager.updateTilt().isSuccess()) {
                        isTiltCompleted = true;
                    }
                    state = State.CHECK_ROUTE_FIELD_AGAIN;
                    break;

                case CHECK_ROUTE_FIELD_AGAIN:
                    if (!isRouteFieldUpdated && gameStateManager.updateTravelFieldState().isSuccess()) {
                        isRouteFieldUpdated = true;
                    }
                    if (!gameService.checkFailCondition() &&
                        (firstPlayer.getAllDiceCount() > 0 || secondPlayer.getAllDiceCount() > 0)) {
                        state = State.PLAY_TURN_FIRST;
                    } else {
                        state = State.ROUND_FINISH;
                    }
                    break;

                case ROUND_FINISH:
                    finishRound();
                    if (gameService.checkFailCondition()) {
                        endgame = true;
                        gameGUI.createFinalWindow(false);
                        state = State.ENDGAME;
                    } else if (lastRound) {
                        state = State.FINAL_CHECK;
                    } else {
                        state = State.CHECK_ENDGAME;
                    }
                    break;

                case FINAL_CHECK:
                    if (gameService.checkWinCondition()) {
                        gameGUI.createFinalWindow(true);
                    } else {
                        gameGUI.createFinalWindow(false);
                    }
                    endgame = true;
                    state = State.ENDGAME;
                    break;

                case ENDGAME:
                    gameGUI.createFinalWindow(false);
                    endgame = true;
                    break;
            }
            if (endgame) {
                break;
            }
        }
    }

    /**
     * Zeichnet die GUI-Komponenten und führt Aktionen aus.
     */
    public void render() {
        gameGUI.stage.act(Gdx.graphics.getDeltaTime());
        gameGUI.stage.draw();
    }

    /**
     * Aktualisiert die Größenanpassung der GUI bei Änderung der Fenstergröße.
     *
     * @param width  Neue Breite des Fensters.
     * @param height Neue Höhe des Fensters.
     */
    public void resize(int width, int height) {
        gameGUI.stage.getViewport().update(width, height, true);
    }

    /**
     * Gibt alle Ressourcen der GUI frei.
     */
    public void dispose() {
        if (gameGUI != null) {
            gameGUI.dispose();
        }
    }
}
