package com.teama.skyteam.drop.application.gamestate;

import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.model.game.SpielConstants;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.application.logic.events.SpeedChangedEvent;
import com.teama.skyteam.drop.application.logic.events.TiltChangedEvent;
import com.teama.skyteam.drop.domain.model.spieler.Spieler;
import com.teama.skyteam.drop.domain.model.szenario.ReiseFeld;
import com.teama.skyteam.drop.domain.model.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.domain.model.tokens.KaffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.NeuWurfToken;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StandardGameStateManager implements GameStateManager{
    private final Spiel spiel;

    public StandardGameStateManager(Spiel spiel) {
        this.spiel = spiel;
    }
    @Override
    public ActionResult handleRadio(Wuerfel wuerfel){
        int wert = wuerfel.getWert();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        int currentFieldIndex = spiel.getCurrentPosition();
        int targetField = wert + currentFieldIndex - 1;

        if (!routeSzenario.containsKey(targetField)) {
            return ActionResult.fail("Die Zielposition liegt außerhalb des Szenarios.");
        }

        ReiseFeld zielFeld = routeSzenario.get(targetField);
        if (zielFeld.getAnzahlFlugzeug() > 0) {
            zielFeld.removeFlugzeug();
        }
        return ActionResult.success();
    }

    @Override
    public ActionResult updateTilt(){
        boolean neigenFeldPilotBelegt = spiel.getFeldPilot(FeldName.NEIGEN).isBelegt();
        boolean neigenFeldCopilotBelegt = spiel.getFeldCopilot(FeldName.NEIGEN).isBelegt();

        if (!neigenFeldPilotBelegt || !neigenFeldCopilotBelegt) {
            return ActionResult.success();
        }

        Wuerfel w1 = spiel.getFeldPilot(FeldName.NEIGEN).getWuerfel();
        Wuerfel w2 = spiel.getFeldCopilot(FeldName.NEIGEN).getWuerfel();
        spiel.getFlugzeug().neigen(spiel.getFlugzeug().getNeigung(), new Wuerfel[]{w1, w2});

        float targetRotation;
        if (spiel.getFlugzeug().getNeigung() == 0) {
            targetRotation = 0;
        } else {
            targetRotation = SpielConstants.WHEEL_ANGLE * spiel.getFlugzeug().getNeigung();
        }

        return ActionResult.success()
            .withEvent(new TiltChangedEvent(targetRotation));
    }

    @Override
    public ActionResult updateTravelFieldState (){
        boolean speedFeldPilotBelegt = spiel.getFeldPilot(FeldName.SPEED).isBelegt();
        boolean speedFeldCopilotBelegt = spiel.getFeldCopilot(FeldName.SPEED).isBelegt();
        if (speedFeldPilotBelegt && speedFeldCopilotBelegt) {
            int pilotSpeed = spiel.getFeldPilot(FeldName.SPEED).getWuerfel().getWert();
            int copilotSpeed = spiel.getFeldCopilot(FeldName.SPEED).getWuerfel().getWert();
            int totalSpeed = pilotSpeed + copilotSpeed;
            spiel.getFlugzeug().setSpeedAktuell(totalSpeed);
            int speedUntereGrenze = spiel.getFlugzeug().getSpeedUntereGrenze().getValue();
            int speedObereGrenze = spiel.getFlugzeug().getSpeedObereGrenze().getValue();
            int steps;
            if (totalSpeed < speedUntereGrenze) {
                steps = 0;
            } else if (totalSpeed <= speedObereGrenze) {
                steps = 1;
            } else {
                steps = 2;
            }
            moveForward(steps);
            spiel.setCurrentSpeed(steps);
            return ActionResult.success();
        }
        return ActionResult.fail("Error bei der Setzung der Speed");
    }

    @Override
    public ActionResult moveForward(int steps){
        int position = spiel.getCurrentPosition();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        int newPosition = position + steps;
        if (newPosition >= routeSzenario.size()) {
            spiel.setKollision(true);
        } else if (steps > 2) {
            return ActionResult.fail("Der Schritt ist zu groß!");
        } else {
            for (int i = position; i <= newPosition; i++) {
                ReiseFeld currentField = routeSzenario.get(i);
                if (currentField.getAnzahlFlugzeug() > 0) {
                    spiel.setKollision(true);
                }
            }
            spiel.setPosition(newPosition);
            spiel.updateCurrentField();
            // szenarioHoeheManager.updateScenario(steps);
        }
        return ActionResult.success();
    }

    @Override
    public void reduceAltitude(){
        List<Flughoehe> hoeheRoute = spiel.getHoeheSzenario();
        if (hoeheRoute.size() > 1) {
            hoeheRoute.removeFirst();
        }
    }

    @Override
    public void addNeuWurfTokens(){
        Flughoehe currentHoehe = spiel.getCurrentHoehe();
        if (currentHoehe.hatErneuWerfen()) {
            spiel.neuWurfTokenAnzahlErhoehen();
        }
    }

    @Override
    public KaffeeToken kaffeeToken() {
        ArrayList<KaffeeToken> kaffeeTokens = spiel.getKaffeeTokens();
        KaffeeToken token = kaffeeTokens.getFirst();
        kaffeeTokens.removeFirst();
        return token;
    }

    @Override
    public NeuWurfToken neuWurfToken() {
        ArrayList<NeuWurfToken> neuWurfTokens = spiel.getNeuWurfTokens();
        NeuWurfToken token = neuWurfTokens.getFirst();
        neuWurfTokens.removeFirst();
        return token;
    }

    @Override
    public void setNeuWuerfels(Spieler spieler) {
        spieler.setWuerfels(werfen());
    }

    @Override
    public ArrayList<Wuerfel> werfen() {
        ArrayList<Wuerfel> wurfe = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wurfe.add(new Wuerfel());
        }
        return wurfe;
    }

    @Override
    public ActionResult checkAndUpdateSpeed() {
        boolean speedFeldPilotBelegt = spiel.getFeldPilot(FeldName.SPEED).isBelegt();
        boolean speedFeldCopilotBelegt = spiel.getFeldCopilot(FeldName.SPEED).isBelegt();

        if (!speedFeldPilotBelegt || !speedFeldCopilotBelegt) {
            return ActionResult.success();
        }

        int pilotSpeed = spiel.getFeldPilot(FeldName.SPEED).getWuerfel().getWert();
        int copilotSpeed = spiel.getFeldCopilot(FeldName.SPEED).getWuerfel().getWert();
        int totalSpeed = pilotSpeed + copilotSpeed;

        spiel.getFlugzeug().setSpeedAktuell(totalSpeed);

        int speedUntereGrenze = spiel.getFlugzeug().getSpeedUntereGrenze().getValue();
        int speedObereGrenze = spiel.getFlugzeug().getSpeedObereGrenze().getValue();

        int steps;
        if (totalSpeed < speedUntereGrenze) {
            steps = 0;
        } else if (totalSpeed <= speedObereGrenze) {
            steps = 1;
        } else {
            steps = 2;
        }

        moveForward(steps);
        spiel.setCurrentSpeed(steps);

        return ActionResult.success()
            .withEvent(new SpeedChangedEvent(steps));
    }

}
