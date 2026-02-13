package com.teama.skyteam.drop.application.gameservice;


import com.badlogic.gdx.Gdx;
import com.teama.skyteam.drop.application.logic.events.*;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.action.factory.ActionFactory;
import com.teama.skyteam.drop.domain.action.validator.ActionValidator;
import com.teama.skyteam.drop.domain.action.validator.ValidationResult;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.logic.observer.GameStateObserver;
import com.teama.skyteam.drop.domain.rules.GameRules;
import com.teama.skyteam.drop.domain.model.spieler.Copilot;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.spieler.Spieler;

import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;
import com.teama.skyteam.drop.domain.model.game.Spiel;

import java.util.ArrayList;
import java.util.List;


/**
 * Die Klasse SpielLogik enthält die Hauptlogik und Steuerung des Spiels.
 * Sie definiert, wie Spieleraktionen verarbeitet werden und wie der Spielstatus aktualisiert wird.
 */

public class SpielLogik {
    private final Spiel spiel;

    private final GameRules gameRules;
    private final ActionValidator validator;
    private final ActionFactory actionFactory;
    private final GameStateManager gameStateManager;
    private final List<GameStateObserver> observers = new ArrayList<>();

    public SpielLogik(Spiel spiel, GameRules gameRules, ActionValidator validator, ActionFactory actionFactory, GameStateManager gameStateManager) {
        this.spiel = spiel;
        this.gameRules = gameRules;

//        this.validator = new StandardActionValidator(); // TODO: Check this? Move to constr?
//        this.gameStateManager = new StandardGameStateManager(spiel);
//        this.actionFactory = new ActionFactory(spiel, gameStateManager);

        this.validator = validator;
        this.gameStateManager = gameStateManager;
        this.actionFactory = actionFactory;

    }

    /**
     * Überprüft, ob eine Ausfallbedingung im Spiel erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    public boolean checkAusfallBedingung() {
        return gameRules.checkAusfallBedingung(spiel);
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    public boolean checkFinalRound() {
        return gameRules.checkFinalRound(spiel);
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    public boolean checkGewinnBedingung() {
        return gameRules.checkGewinnBedingung(spiel);
    }


    public ActionResult pilotSchritt(Spieler spieler, ActionName auswahl, Wuerfel wuerfel) {
        System.err.println("=== pilotSchritt called: action=" + auswahl + " wuerfel=" + wuerfel.getWert());

        ValidationResult validation = validator.validatePilotAction(spiel, auswahl, wuerfel);
        if (!validation.isValid()) {
            System.err.println("=== Validation failed: " + validation.getErrorMsg());
            return ActionResult.fail(validation.getErrorMsg());
        }
        Pilot pilot = (Pilot) spieler;
        PlayerAction action = actionFactory.createPilotAction(auswahl, pilot, wuerfel);
        ActionResult result = action.execute();
        System.err.println("=== Action executed, events count: " + result.getEvents().size());
        for (GameEvent event : result.getEvents()) {
            handleEvent(event);
        }
        checkSpeed();
        checkTilt();

        return result;
    }

    public ActionResult copilotSchritt(Spieler spieler, ActionName auswahl, Wuerfel wuerfel) {
        ValidationResult validation = validator.validateCopilotAction(spiel, auswahl, wuerfel);
        if (!validation.isValid()) {
            return ActionResult.fail(validation.getErrorMsg());
        }
        Copilot copilot = (Copilot) spieler;
        PlayerAction action = actionFactory.createCopilotAction(auswahl, copilot, wuerfel);
        ActionResult result = action.execute();
        for (GameEvent event : result.getEvents()) {
            handleEvent(event);
        }
        checkSpeed();
        checkTilt();

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

    private void notifyChassisActivated(int schalterIndex, int count) {
        for (GameStateObserver observer : observers) {
            observer.onChassisActivated(schalterIndex, count);
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
        Gdx.app.log("SpielLogik", "handleEvent called with: " + event.getClass().getSimpleName());

        if (event instanceof BrakesActivatedEvent) {
            BrakesActivatedEvent e = (BrakesActivatedEvent) event;
            System.err.println("=== Brakes: schalterIndex=" + e.getSchalterIndex() + " indicatorValue=" + e.getIndicatorValue());
            notifyBrakesActivated(e.getSchalterIndex(), e.getIndicatorValue());
            Gdx.app.log("SpielLogik", "Brakes activated: schalterIndex=" + e.getSchalterIndex() + " indicatorValue=" + e.getIndicatorValue());
        } else if (event instanceof ChassisActivatedEvent) {
            ChassisActivatedEvent e = (ChassisActivatedEvent) event;
            System.err.println("=== Chassis: schalterIndex=" + e.getSchalterIndex() + " anzahl=" + e.getAnzahl());
            notifyChassisActivated(e.getSchalterIndex(), e.getAnzahl());
            Gdx.app.log("SpielLogik", "Chassis activated: schalterIndex=" + e.getSchalterIndex() + " anzahl=" + e.getAnzahl());
        } else if (event instanceof FlapsActivatedEvent) {
            FlapsActivatedEvent e = (FlapsActivatedEvent) event;
            System.err.println("=== Flaps: schalterIndex=" + e.getSchalterIndex() + " state=" + e.getState());
            notifyFlapsActivated(e.getSchalterIndex(), e.getState());
            Gdx.app.log("SpielLogik", "Flaps activated: schalterIndex=" + e.getSchalterIndex() + " state=" + e.getState());

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
    //void radio(Wuerfel gewaehlterWuerfel) {}

    // Changed/Moved to StandardGameStateManager/updateTilt
    //boolean neigenSetzen() {}

    // Changed/Moved to StandardGameStateManager/moveForward
    //void vorwaertsBewegen(int steps) {}


    //Changed/Moved to StandardGameStateManager/addNeuWurfTokens
    // void addNeuWurfTokens() {}


    // Changed/Moved to StandardGameStateManager/reduceAltitude
    //void hoeheReduzieren() {}

    //void setNeuWuerfels(Spieler spieler) {}

    //private ArrayList<Wuerfel> werfen() {}



    // Changed/Moved to StandardFieldManager
    //void clearManyUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void removeDiceFromOneUseFelds(ArrayList<Feld> oneUseFelds) {}


    // Changed/Moved to StandardFieldManager
    //ArrayList<Feld> getOneUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void clearKaffeeFeld() { }


}
