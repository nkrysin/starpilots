package com.teama.skyteam.drop.spiel.gamestate;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.SpielConstants;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.validator.ValidationResult;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StandardGameStateManager implements GameStateManager{
    private final Spiel spiel;

    public StandardGameStateManager(Spiel spiel) {
        this.spiel = spiel;
    }
    @Override
    public ValidationResult handleRadio(Wuerfel wuerfel){
        int wert = wuerfel.getWert();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        int currentFieldIndex = spiel.getCurrentPosition();
        int targetField = wert + currentFieldIndex - 1;
        if (!routeSzenario.containsKey(targetField)) {
            return ValidationResult.fail("Die Zielposition liegt außerhalb des Szenarios.");
        } else {
            ReiseFeld zielFeld = routeSzenario.get(targetField);
            if (zielFeld.getAnzahlFlugzeug() > 0) {
                zielFeld.removeFlugzeug();
            }
            return ValidationResult.success();
        }

    }

    @Override
    public ValidationResult updateTilt(){
        boolean neigenFeldPilotBelegt = spiel.getFeldPilot(FeldName.NEIGEN).isBelegt();
        boolean neigenFeldCopilotBelegt = spiel.getFeldCopilot(FeldName.NEIGEN).isBelegt();
        if (neigenFeldPilotBelegt && neigenFeldCopilotBelegt) {
            Wuerfel w1 = spiel.getFeldPilot(FeldName.NEIGEN).getWuerfel();
            Wuerfel w2 = spiel.getFeldCopilot(FeldName.NEIGEN).getWuerfel();
            spiel.getFlugzeug().neigen(spiel.getFlugzeug().getNeigung(), new Wuerfel[]{w1, w2});
            return ValidationResult.success();
        }
        return ValidationResult.fail("Error bei der Setzung der Neigung");
    }

    @Override
    public ValidationResult updateTravelFieldState (SzenarioHoeheManager szenarioHoeheManager){
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
            moveForward(steps, szenarioHoeheManager);
            spiel.setCurrentSpeed(steps);
            return ValidationResult.success();
        }
        return ValidationResult.fail("Error bei der Setzung der Speed");
    }

    @Override
    public ValidationResult moveForward(int steps, SzenarioHoeheManager szenarioHoeheManager){
        int position = spiel.getCurrentPosition();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        int newPosition = position + steps;
        if (newPosition >= routeSzenario.size()) {
            spiel.setKollision(true);
        } else if (steps > 2) {
            return ValidationResult.fail("Der Schritt ist zu groß!");
        } else {
            for (int i = position; i <= newPosition; i++) {
                ReiseFeld currentField = routeSzenario.get(i);
                if (currentField.getAnzahlFlugzeug() > 0) {
                    spiel.setKollision(true);
                }
            }
            spiel.setPosition(newPosition);
            spiel.updateCurrentField();
            szenarioHoeheManager.updateScenario(steps);
        }
        return ValidationResult.success();
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

}
