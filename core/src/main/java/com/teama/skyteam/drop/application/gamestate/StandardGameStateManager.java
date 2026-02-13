package com.teama.skyteam.drop.application.gamestate;

import com.teama.skyteam.drop.domain.model.campaign.RouteField;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.game.GameConstants;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.application.logic.events.SpeedChangedEvent;
import com.teama.skyteam.drop.application.logic.events.TiltChangedEvent;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.domain.model.campaign.altitude.Altitude;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StandardGameStateManager implements GameStateManager{
    private final Game game;

    public StandardGameStateManager(Game game) {
        this.game = game;
    }
    @Override
    public ActionResult handleRadio(Dice dice){
        int wert = dice.getWert();
        TreeMap<Integer, RouteField> routeSzenario = game.getRouteSzenario();
        int currentFieldIndex = game.getCurrentPosition();
        int targetField = wert + currentFieldIndex - 1;

        if (!routeSzenario.containsKey(targetField)) {
            return ActionResult.fail("Die Zielposition liegt außerhalb des Szenarios.");
        }

        RouteField zielFeld = routeSzenario.get(targetField);
        if (zielFeld.getAnzahlFlugzeug() > 0) {
            zielFeld.removeFlugzeug();
        }
        return ActionResult.success();
    }

    @Override
    public ActionResult updateTilt(){
        boolean neigenFeldPilotBelegt = game.getFeldPilot(FieldName.NEIGEN).isBelegt();
        boolean neigenFeldCopilotBelegt = game.getFeldCopilot(FieldName.NEIGEN).isBelegt();

        if (!neigenFeldPilotBelegt || !neigenFeldCopilotBelegt) {
            return ActionResult.success();
        }

        Dice w1 = game.getFeldPilot(FieldName.NEIGEN).getWuerfel();
        Dice w2 = game.getFeldCopilot(FieldName.NEIGEN).getWuerfel();
        game.getFlugzeug().neigen(game.getFlugzeug().getTilt(), new Dice[]{w1, w2});

        float targetRotation;
        if (game.getFlugzeug().getTilt() == 0) {
            targetRotation = 0;
        } else {
            targetRotation = GameConstants.WHEEL_ANGLE * game.getFlugzeug().getTilt();
        }

        return ActionResult.success()
            .withEvent(new TiltChangedEvent(targetRotation));
    }

    @Override
    public ActionResult updateTravelFieldState (){
        boolean speedFeldPilotBelegt = game.getFeldPilot(FieldName.SPEED).isBelegt();
        boolean speedFeldCopilotBelegt = game.getFeldCopilot(FieldName.SPEED).isBelegt();
        if (speedFeldPilotBelegt && speedFeldCopilotBelegt) {
            int pilotSpeed = game.getFeldPilot(FieldName.SPEED).getWuerfel().getWert();
            int copilotSpeed = game.getFeldCopilot(FieldName.SPEED).getWuerfel().getWert();
            int totalSpeed = pilotSpeed + copilotSpeed;
            game.getFlugzeug().setCurrentSpeed(totalSpeed);
            int speedUntereGrenze = game.getFlugzeug().getSpeedLowerLimit().getValue();
            int speedObereGrenze = game.getFlugzeug().getSpeedUpperLimit().getValue();
            int steps;
            if (totalSpeed < speedUntereGrenze) {
                steps = 0;
            } else if (totalSpeed <= speedObereGrenze) {
                steps = 1;
            } else {
                steps = 2;
            }
            moveForward(steps);
            game.setCurrentSpeed(steps);
            return ActionResult.success();
        }
        return ActionResult.fail("Error bei der Setzung der Speed");
    }

    @Override
    public ActionResult moveForward(int steps){
        int position = game.getCurrentPosition();
        TreeMap<Integer, RouteField> routeSzenario = game.getRouteSzenario();
        int newPosition = position + steps;
        if (newPosition >= routeSzenario.size()) {
            game.setKollision(true);
        } else if (steps > 2) {
            return ActionResult.fail("Der Schritt ist zu groß!");
        } else {
            for (int i = position; i <= newPosition; i++) {
                RouteField currentField = routeSzenario.get(i);
                if (currentField.getAnzahlFlugzeug() > 0) {
                    game.setKollision(true);
                }
            }
            game.setPosition(newPosition);
            game.updateCurrentField();
            // szenarioHoeheManager.updateScenario(steps);
        }
        return ActionResult.success();
    }

    @Override
    public void reduceAltitude(){
        List<Altitude> hoeheRoute = game.getHoeheSzenario();
        if (hoeheRoute.size() > 1) {
            hoeheRoute.removeFirst();
        }
    }

    @Override
    public void addNeuWurfTokens(){
        Altitude currentHoehe = game.getCurrentHoehe();
        if (currentHoehe.hatErneuWerfen()) {
            game.neuWurfTokenAnzahlErhoehen();
        }
    }

    @Override
    public CoffeeToken kaffeeToken() {
        ArrayList<CoffeeToken> coffeeTokens = game.getKaffeeTokens();
        CoffeeToken token = coffeeTokens.getFirst();
        coffeeTokens.removeFirst();
        return token;
    }

    @Override
    public RerollToken neuWurfToken() {
        ArrayList<RerollToken> rerollTokens = game.getNeuWurfTokens();
        RerollToken token = rerollTokens.getFirst();
        rerollTokens.removeFirst();
        return token;
    }

    @Override
    public void setNeuWuerfels(Player player) {
        player.setWuerfels(werfen());
    }

    @Override
    public ArrayList<Dice> werfen() {
        ArrayList<Dice> wurfe = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wurfe.add(new Dice());
        }
        return wurfe;
    }

    @Override
    public ActionResult checkAndUpdateSpeed() {
        boolean speedFeldPilotBelegt = game.getFeldPilot(FieldName.SPEED).isBelegt();
        boolean speedFeldCopilotBelegt = game.getFeldCopilot(FieldName.SPEED).isBelegt();

        if (!speedFeldPilotBelegt || !speedFeldCopilotBelegt) {
            return ActionResult.success();
        }

        int pilotSpeed = game.getFeldPilot(FieldName.SPEED).getWuerfel().getWert();
        int copilotSpeed = game.getFeldCopilot(FieldName.SPEED).getWuerfel().getWert();
        int totalSpeed = pilotSpeed + copilotSpeed;

        game.getFlugzeug().setCurrentSpeed(totalSpeed);

        int speedUntereGrenze = game.getFlugzeug().getSpeedLowerLimit().getValue();
        int speedObereGrenze = game.getFlugzeug().getSpeedUpperLimit().getValue();

        int steps;
        if (totalSpeed < speedUntereGrenze) {
            steps = 0;
        } else if (totalSpeed <= speedObereGrenze) {
            steps = 1;
        } else {
            steps = 2;
        }

        moveForward(steps);
        game.setCurrentSpeed(steps);

        return ActionResult.success()
            .withEvent(new SpeedChangedEvent(steps));
    }

}
