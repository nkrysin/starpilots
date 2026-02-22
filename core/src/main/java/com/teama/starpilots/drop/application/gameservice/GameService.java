package com.teama.starpilots.drop.application.gameservice;


import com.badlogic.gdx.Gdx;
import com.teama.starpilots.drop.application.logic.events.*;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.domain.action.factory.ActionFactory;
import com.teama.starpilots.drop.domain.action.validator.ActionValidator;
import com.teama.starpilots.drop.domain.action.validator.ValidationResult;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.application.gamestate.GameStateManager;
import com.teama.starpilots.drop.application.logic.observer.GameStateObserver;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.model.player.Player;
import com.teama.starpilots.drop.domain.model.tokens.Dice;
import com.teama.starpilots.drop.domain.rules.GameRules;
import com.teama.starpilots.drop.domain.model.player.Copilot;
import com.teama.starpilots.drop.domain.model.player.Pilot;

import java.util.ArrayList;
import java.util.List;


/**
 * Die Klasse GameService enthält die Hauptlogik und Steuerung des Spiels.
 * Sie definiert, wie Spieleraktionen verarbeitet werden und wie der Spielstatus aktualisiert wird.
 */

public class GameService {
    private final Game game;

    private final GameRules gameRules;
    private final ActionValidator validator;
    private final ActionFactory actionFactory;
    private final GameStateManager gameStateManager;
    private final List<GameStateObserver> observers = new ArrayList<>();

    public GameService(Game game, GameRules gameRules, ActionValidator validator, ActionFactory actionFactory, GameStateManager gameStateManager) {
        this.game = game;
        this.gameRules = gameRules;

        this.validator = validator;
        this.gameStateManager = gameStateManager;
        this.actionFactory = actionFactory;

    }

    /**
     * Überprüft, ob eine Ausfallbedingung im Game erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    public boolean checkFailCondition() {
        return gameRules.checkFailCondition(game);
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    public boolean checkFinalRound() {
        return gameRules.checkFinalRound(game);
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    public boolean checkWinCondition() {
        return gameRules.checkWinCondition(game);
    }


    public ActionResult pilotStep(Player player, ActionName auswahl, Dice dice) {
        System.err.println("=== pilotStep called: action=" + auswahl + " dice=" + dice.getValue());

        ValidationResult validation = validator.validatePilotAction(game, auswahl, dice);
        if (!validation.isValid()) {
            System.err.println("=== Validation failed: " + validation.getErrorMsg());
            return ActionResult.fail(validation.getErrorMsg());
        }
        Pilot pilot = (Pilot) player;
        PlayerAction action = actionFactory.createPilotAction(auswahl, pilot, dice);
        ActionResult result = action.execute();
        System.err.println("=== Action executed, events count: " + result.getEvents().size());
        for (GameEvent event : result.getEvents()) {
            handleEvent(event);
        }

        return result;
    }

    public ActionResult copilotStep(Player player, ActionName auswahl, Dice dice) {
        ValidationResult validation = validator.validateCopilotAction(game, auswahl, dice);
        if (!validation.isValid()) {
            return ActionResult.fail(validation.getErrorMsg());
        }
        Copilot copilot = (Copilot) player;
        PlayerAction action = actionFactory.createCopilotAction(auswahl, copilot, dice);
        ActionResult result = action.execute();
        for (GameEvent event : result.getEvents()) {
            handleEvent(event);
        }

        return result;
    }

    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    private void notifyBrakesActivated(int schalterIndex, int indicatorValue) {
        for (GameStateObserver observer : observers) {
            observer.onBrakesActivated(schalterIndex, indicatorValue);
        }
    }

    private void notifyTiltChanged(float targetRotation) {
        for (GameStateObserver observer : observers) {
            observer.onTiltChanged(targetRotation);
        }
    }

    private void notifyLandingGearActivated(int schalterIndex, int count) {
        for (GameStateObserver observer : observers) {
            observer.onLandingGearActivated(schalterIndex, count);
        }
    }

    private void notifyFlapsActivated(int schalterIndex, int state) {
        for (GameStateObserver observer : observers) {
            observer.onFlapsActivated(schalterIndex, state);
        }
    }

    private void notifySpeedChanged(int step) {
        for (GameStateObserver observer : observers) {
            observer.onSpeedChanged(step);
        }
    }

    private void handleEvent(GameEvent event) {
        System.err.println("=== handleEvent called with: " + event.getClass().getSimpleName());
        Gdx.app.log("GameService", "handleEvent called with: " + event.getClass().getSimpleName());

        if (event instanceof BrakesActivatedEvent) {
            BrakesActivatedEvent e = (BrakesActivatedEvent) event;
            System.err.println("=== Brakes: schalterIndex=" + e.getSwitchIndex() + " indicatorValue=" + e.getIndicatorValue());
            notifyBrakesActivated(e.getSwitchIndex(), e.getIndicatorValue());
            Gdx.app.log("GameService", "Brakes activated: schalterIndex=" + e.getSwitchIndex() + " indicatorValue=" + e.getIndicatorValue());
        } else if (event instanceof LandingGearActivatedEvent) {
            LandingGearActivatedEvent e = (LandingGearActivatedEvent) event;
            System.err.println("=== LandingGear: schalterIndex=" + e.getSwitchIndex() + " anzahl=" + e.getCount());
            notifyLandingGearActivated(e.getSwitchIndex(), e.getCount());
            Gdx.app.log("GameService", "LandingGear activated: schalterIndex=" + e.getSwitchIndex() + " anzahl=" + e.getCount());
        } else if (event instanceof FlapsActivatedEvent) {
            FlapsActivatedEvent e = (FlapsActivatedEvent) event;
            System.err.println("=== Flaps: schalterIndex=" + e.getSwitchIndex() + " state=" + e.getState());
            notifyFlapsActivated(e.getSwitchIndex(), e.getState());
            Gdx.app.log("GameService", "Flaps activated: schalterIndex=" + e.getSwitchIndex() + " state=" + e.getState());

        }
    }

    public ActionResult checkSpeed() {
        ActionResult result = gameStateManager.checkAndUpdateSpeed();
        for (GameEvent event : result.getEvents()) {
            if (event instanceof SpeedChangedEvent) {
                SpeedChangedEvent e = (SpeedChangedEvent) event;
                notifySpeedChanged(e.getSteps());
            }
        }
        return result;
    }

    public ActionResult checkTilt() {
        ActionResult result = gameStateManager.updateTilt();

        for (GameEvent event : result.getEvents()) {
            if (event instanceof TiltChangedEvent) {
                TiltChangedEvent e = (TiltChangedEvent) event;
                notifyTiltChanged(e.getTargetRotation());
            }
        }
        return result;
    }







    // Changed/Moved to StandardGameStateManager/updateTravelFieldState
    //boolean reiseFeldZustandAktualisieren() {};

    // Changed/Moved to StandardGameStateManager/handleRadio
    //void radio(Dice gewaehlterWuerfel) {}

    // Changed/Moved to StandardGameStateManager/updateTilt
    //boolean neigenSetzen() {}

    // Changed/Moved to StandardGameStateManager/moveForward
    //void vorwaertsBewegen(int steps) {}


    //Changed/Moved to StandardGameStateManager/addRerollTokens
    // void addRerollTokens() {}


    // Changed/Moved to StandardGameStateManager/reduceAltitude
    //void hoeheReduzieren() {}

    //void setNewDices(Player spieler) {}

    //private ArrayList<Dice> createRandomDice() {}



    // Changed/Moved to StandardFieldManager
    //void clearManyUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void removeDiceFromOneUseFelds(ArrayList<Field> oneUseFelds) {}


    // Changed/Moved to StandardFieldManager
    //ArrayList<Field> getOneUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void clearKaffeeFeld() { }


}
