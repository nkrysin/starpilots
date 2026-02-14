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
        int wert = dice.getValue();
        TreeMap<Integer, RouteField> routeSzenario = game.getRouteCampaign();
        int currentFieldIndex = game.getCurrentPosition();
        int targetField = wert + currentFieldIndex - 1;

        if (!routeSzenario.containsKey(targetField)) {
            return ActionResult.fail("Die Zielposition liegt außerhalb des Szenarios.");
        }

        RouteField zielFeld = routeSzenario.get(targetField);
        if (zielFeld.getPlaneCount() > 0) {
            zielFeld.removePlane();
        }
        return ActionResult.success();
    }

    @Override
    public ActionResult updateTilt(){
        boolean neigenFeldPilotBelegt = game.getFieldPilot(FieldName.TILT).isOccupied();
        boolean neigenFeldCopilotBelegt = game.getFieldCopilot(FieldName.TILT).isOccupied();

        if (!neigenFeldPilotBelegt || !neigenFeldCopilotBelegt) {
            return ActionResult.success();
        }

        Dice w1 = game.getFieldPilot(FieldName.TILT).getDice();
        Dice w2 = game.getFieldCopilot(FieldName.TILT).getDice();
        game.getPlane().tilt(game.getPlane().getTilt(), new Dice[]{w1, w2});

        float targetRotation;
        if (game.getPlane().getTilt() == 0) {
            targetRotation = 0;
        } else {
            targetRotation = GameConstants.WHEEL_ANGLE * game.getPlane().getTilt();
        }

        return ActionResult.success()
            .withEvent(new TiltChangedEvent(targetRotation));
    }

    @Override
    public ActionResult updateTravelFieldState (){
        boolean speedFeldPilotBelegt = game.getFieldPilot(FieldName.SPEED).isOccupied();
        boolean speedFeldCopilotBelegt = game.getFieldCopilot(FieldName.SPEED).isOccupied();
        if (speedFeldPilotBelegt && speedFeldCopilotBelegt) {
            int pilotSpeed = game.getFieldPilot(FieldName.SPEED).getDice().getValue();
            int copilotSpeed = game.getFieldCopilot(FieldName.SPEED).getDice().getValue();
            int totalSpeed = pilotSpeed + copilotSpeed;
            game.getPlane().setCurrentSpeed(totalSpeed);
            int speedUntereGrenze = game.getPlane().getSpeedLowerLimit().getValue();
            int speedObereGrenze = game.getPlane().getSpeedUpperLimit().getValue();
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
        TreeMap<Integer, RouteField> routeSzenario = game.getRouteCampaign();
        int newPosition = position + steps;
        if (newPosition >= routeSzenario.size()) {
            game.setCollision(true);
        } else if (steps > 2) {
            return ActionResult.fail("Der Schritt ist zu groß!");
        } else {
            for (int i = position; i <= newPosition; i++) {
                RouteField currentField = routeSzenario.get(i);
                if (currentField.getPlaneCount() > 0) {
                    game.setCollision(true);
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
        List<Altitude> hoeheRoute = game.getAltitudeCampaign();
        if (hoeheRoute.size() > 1) {
            hoeheRoute.removeFirst();
        }
    }

    @Override
    public void addRerollTokens(){
        Altitude currentHoehe = game.getCurrentAltitude();
        if (currentHoehe.hasRerollToken()) {
            game.incrementRerollTokenCount();
        }
    }

    @Override
    public CoffeeToken takeCoffeeToken() {
        ArrayList<CoffeeToken> coffeeTokens = game.getCoffeeTokens();
        CoffeeToken token = coffeeTokens.getFirst();
        coffeeTokens.removeFirst();
        return token;
    }

    @Override
    public RerollToken takeRerollToken() {
        ArrayList<RerollToken> rerollTokens = game.getRerollTokens();
        RerollToken token = rerollTokens.getFirst();
        rerollTokens.removeFirst();
        return token;
    }

    @Override
    public void setNewDices(Player player) {
        player.setAllDice(createRandomDice());
    }

    @Override
    public ArrayList<Dice> createRandomDice() {
        ArrayList<Dice> wurfe = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wurfe.add(new Dice());
        }
        return wurfe;
    }

    @Override
    public ActionResult checkAndUpdateSpeed() {
        boolean speedFeldPilotBelegt = game.getFieldPilot(FieldName.SPEED).isOccupied();
        boolean speedFeldCopilotBelegt = game.getFieldCopilot(FieldName.SPEED).isOccupied();

        if (!speedFeldPilotBelegt || !speedFeldCopilotBelegt) {
            return ActionResult.success();
        }

        int pilotSpeed = game.getFieldPilot(FieldName.SPEED).getDice().getValue();
        int copilotSpeed = game.getFieldCopilot(FieldName.SPEED).getDice().getValue();
        int totalSpeed = pilotSpeed + copilotSpeed;

        game.getPlane().setCurrentSpeed(totalSpeed);

        int speedUntereGrenze = game.getPlane().getSpeedLowerLimit().getValue();
        int speedObereGrenze = game.getPlane().getSpeedUpperLimit().getValue();

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
