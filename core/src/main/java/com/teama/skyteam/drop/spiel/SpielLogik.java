package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.flugzeug.Klappen;
import com.teama.skyteam.drop.spieler.*;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.*;

public class SpielLogik {
    private final Spiel spiel;

    public SpielLogik(Spiel spiel) {
        this.spiel = spiel;
    }
    //Nur die Beschreibung von internen Methoden

    boolean checkAusfallBedingung() {
        boolean bedingungKollision = spiel.isKollision();
        boolean bedingungWenigerAlsSzenarioLange = spiel.getCurrentPosition() > spiel.getRouteSzenario().size();
        boolean bedingungHoehe = !spiel.getCurrentField().istFlughafen() && (spiel.getCurrentHoehe().getHoehe() == 0);
        boolean bedingungNeigung = (spiel.getFlugzeug().getNeigung() > 2 || spiel.getFlugzeug().getNeigung() < -2);


        boolean ausfallBedingungErfuellt = bedingungKollision || bedingungWenigerAlsSzenarioLange || bedingungHoehe || bedingungNeigung;

        return ausfallBedingungErfuellt;
    }

    boolean checkFinalRound(){
        boolean bedingungFlughafen = spiel.getCurrentField().istFlughafen();
        boolean bedingungHoehe = (spiel.getCurrentHoehe().getHoehe() == 0);

        return bedingungFlughafen && bedingungHoehe;
    }


    boolean checkGewinnBedingung() {

        boolean bedingungIstFrei = (spiel.getCurrentField().getAnzahlFlugzeug() == 0);
        boolean bedingungNeigung = (spiel.getFlugzeug().getNeigung() == 0);
        boolean bedingungKlappen = (spiel.getFlugzeug().getKlappenZustand() == Klappen.LEVEL4);
        boolean bedingungFahrgestelle = (spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle() == 3);
        boolean bedingungSpeed = (spiel.getFlugzeug().getSpeedAktuell() < spiel.getFlugzeug().getBremsenZustand().getValue());

        boolean gewinnBedingungErfuellt = bedingungIstFrei &&
                bedingungNeigung && bedingungKlappen && bedingungFahrgestelle && bedingungSpeed;

        return gewinnBedingungErfuellt;
    }

    boolean checkEndBedingung() {
        return checkAusfallBedingung() || checkFinalRound();
    }


    boolean pilotSchritt(Pilot pilot, int auswahl, Wuerfel gewaehlterWuerfel, int indexWuerfel) {
        switch (auswahl) {
            case 1: // Radio wählen
                if(!spiel.getFeldPilot("radioFeld").isBelegt()){
                    radio(gewaehlterWuerfel);
                    pilot.radioWaehlen(gewaehlterWuerfel, indexWuerfel);
                }
                //"Radio gut."
                return true;
            case 2: // Geschwindigkeit wählen
                pilot.speedWaehlen(gewaehlterWuerfel, indexWuerfel);
                //"Geschwindigkeit wurde erfolgreich gewählt."
                return true;
            case 3: // Neigen wählen
                pilot.neigenWaehlen(gewaehlterWuerfel, indexWuerfel);
                //"Neigen wurde erfolgreich gewählt"
                return true;
            case 4: // Bremsen wählen
                if (spiel.getFlugzeug().bremsen(gewaehlterWuerfel)){
                    pilot.bremsenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    //"Bremsen wurde erfolgreich gewählt."
                    return true;
                }
                else {
                    //"Fehler beim Bremsen"
                    return false;
                }
            case 5: // Fahrgestell wählen
                if (spiel.getFlugzeug().fahrgestellAusfahren(gewaehlterWuerfel)){
                    pilot.fahrgestellWaehlen(gewaehlterWuerfel, indexWuerfel);
                    //"Fahrgestell wurde erfolgreich gewählt"
                    return true;
                }
                else {
                    //TODO Окно Dialog, которое выводит "Fehler beim Fahrgestellen."
                    return false;
                }
            case 6: // Kaffee wählen
                pilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel);
                //"Kaffee wurde erfolgreich gewählt."
                return true;
            default:
                //TODO Окно Dialog, которое выводит "Ungültige Auswahl. Bitte versuchen Sie es erneut."
        }
        return false;
    }

    boolean copilotSchritt(Copilot copilot, int auswahl, Wuerfel gewaehlterWuerfel, int indexWuerfel) {
        switch (auswahl) {
            case 1: // Radio wählen
                if(!spiel.getFeldCopilot("radioFeld1").isBelegt() || !spiel.getFeldCopilot("radioFeld2").isBelegt()){
                    radio(gewaehlterWuerfel);
                    copilot.radioWaehlen(gewaehlterWuerfel, indexWuerfel);
                }
                //"Radio gut."
                return true;
            case 2: // Geschwindigkeit wählen
                copilot.speedWaehlen(gewaehlterWuerfel, indexWuerfel);
                //"Geschwindigkeit wurde erfolgreich gewählt."
                return true;
            case 3: // Neigen wählen
                copilot.neigenWaehlen(gewaehlterWuerfel, indexWuerfel);
                //"Neigen wurde erfolgreich gewählt."
                return true;
            case 4:// Klappen wählen
                if (spiel.getFlugzeug().klappenOeffnen(gewaehlterWuerfel)) {
                    copilot.klappenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    //"Klappen wurde erfolgreich gewählt."
                    return true;
                }
                else {
                    //TODO Окно Dialog, которое выводит "Fehler beim Klappen."
                    return false;
                }
            case 5: // Kaffee wählen
                copilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel);
                return true;
            default:
                //TODO Окно Dialog, которое выводит "Ungültige Auswahl. Bitte versuchen Sie es erneut."
        }
        return false;
    }

    boolean reiseFeldZustandAktualisieren() {

        boolean speedFeldPilotBelegt = spiel.getFeldPilot("speedFeld").isBelegt();
        boolean speedFeldCopilotBelegt = spiel.getFeldCopilot("speedFeld").isBelegt();

        if(speedFeldPilotBelegt && speedFeldCopilotBelegt) {
            int pilotSpeed = spiel.getFeldPilot("speedFeld").getWuerfel().getWert();
            int copilotSpeed = spiel.getFeldCopilot("speedFeld").getWuerfel().getWert();

            int totalSpeed = pilotSpeed + copilotSpeed;
            spiel.getFlugzeug().setSpeedAktuell(totalSpeed);

            int speedUntereGrenze = spiel.getFlugzeug().getSpeedUntereGrenze().getValue();
            int speedObereGrenze = spiel.getFlugzeug().getSpeedObereGrenze().getValue();

            int steps;
            if (totalSpeed < speedUntereGrenze) {
                steps = 0;
            } else if (totalSpeed >= speedUntereGrenze && totalSpeed <= speedObereGrenze) {
                //speedUntereGrenze < totalSpeed < speedObereGrenze
                steps = 1;
            } else {
                steps = 2;
            }

            //"Bewegen um " + steps + " steps"
            vorwaertsBewegen(steps);
            spiel.setCurrentSpeed(steps);

            return true;
        }
        return false;
    }




    KaffeToken kaffeToken() {
        ArrayList<KaffeToken> kaffeTokens = spiel.getKaffeTokens();
        KaffeToken token = kaffeTokens.getFirst();
        kaffeTokens.removeFirst();
        return token;
    }

    NeuWurfToken neuWurfToken() {
        ArrayList<NeuWurfToken> neuWurfTokens = spiel.getNeuWurfTokens();
        NeuWurfToken token = neuWurfTokens.getFirst();
        neuWurfTokens.removeFirst();
        return token;
    }

    void radio(Wuerfel gewaehlterWuerfel) {
        int wert = gewaehlterWuerfel.getWert();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();
        int currentFieldIndex = spiel.getCurrentPosition();
        int targetField = wert + currentFieldIndex - 1;


        if (!routeSzenario.containsKey(targetField)) {
            throw new IllegalArgumentException("Die Zielposition liegt außerhalb des Szenarios.");
        } else {
            ReiseFeld zielFeld = routeSzenario.get(targetField);
            if (zielFeld.getAnzahlFlugzeug() == 0) {
                //"Radio wurde erfolgreich gewählt, aber in diesem Feld liegen keine Flugzeuge"
            } else {
                zielFeld.removeFlugzeug();
                //"Radio wurde erfolgreich gewählt."
            }
        }
    }

    //wir nehmen den Wert aus dem Feld "neigen", es ist obligatorisch, zuerst den Würfel aus dem Feld des Piloten in die Methode einzugeben, dann den des Kopiloten.
    // wir subtrahieren den Wert des Würfels des Kopiloten vom Wert des Würfels des Piloten.
    // abhängig vom Vorzeichen der erhaltenen Differenz wird die Neigung des Flugzeugs bestimmt
    boolean neigenSetzen() {

        boolean neigenFeldPilotBelegt = spiel.getFeldPilot("neigenFeld").isBelegt();
        boolean neigenFeldCopilotBelegt = spiel.getFeldCopilot("neigenFeld").isBelegt();

        if(neigenFeldPilotBelegt && neigenFeldCopilotBelegt) {
            Wuerfel w1 = spiel.getFeldPilot("neigenFeld").getWuerfel();
            Wuerfel w2 = spiel.getFeldCopilot("neigenFeld").getWuerfel();
            spiel.getFlugzeug().neigen(spiel.getFlugzeug().getNeigung(), new Wuerfel[]{w1,w2});

            if(spiel.getFlugzeug().getNeigung() == 0){
                //"Flugzeug ist waagerecht!"
            }
            else if(spiel.getFlugzeug().getNeigung() > 0){ // PILOT
                //"Flugzeug hat sich in Pilots Richtung um " + spiel.getFlugzeug().getNeigung() + " Schritt(e) gedreht!"
            }
            else { //COPILOT
                //"Flugzeug hat sich in Copilots Richtung um " + -spiel.getFlugzeug().getNeigung() + " Schritt(e) gedreht!"
            }

            return true;
        }

        return false;

    }

    void vorwaertsBewegen(int steps) {
        int position = spiel.getCurrentPosition();
        TreeMap<Integer, ReiseFeld> routeSzenario = spiel.getRouteSzenario();

        int newPosition = position + steps;
        if (newPosition >= routeSzenario.size()) {
            spiel.setKollision(true);
        }
        else if (steps > 2) {
            throw new IllegalArgumentException("Der Schritt ist zu groß!");
        }
        else {
           for (int i = position; i <= newPosition; i++) {
                ReiseFeld currentField = routeSzenario.get(i);
                if (currentField.getAnzahlFlugzeug()>0) {
                    spiel.setKollision(true);
                }
            }
            spiel.setPosition(newPosition);
            spiel.updateCurrentField();
        }
    }

    void addNeuWurfTokens() {
        Flughoehe currentHoehe = spiel.getCurrentHoehe();
        if(currentHoehe.hatErneuWerfen()){
            spiel.neuWurfTokenAnzahlErhoehen();
        }
    }

    void hoeheReduzieren() {
        List<Flughoehe> hoeheRoute = spiel.getHoeheSzenario();
        if (hoeheRoute.size() > 1) {
            hoeheRoute.removeFirst();
        }
    }

    void setNeuWuerfels(Spieler spieler) {
        spieler.setWuerfels(werfen());
    }

    private ArrayList<Wuerfel> werfen(){
        ArrayList<Wuerfel> wurfe = new ArrayList<Wuerfel>();
        for(int i = 0; i < 4; i++){
            wurfe.add(new Wuerfel());
        }
        return wurfe;
    }

    void clearManyUseFelds(){
        spiel.getFeldPilot("neigenFeld").setWuerfel(null);
        spiel.getFeldPilot("speedFeld").setWuerfel(null);
        spiel.getFeldPilot("radioFeld").setWuerfel(null);

        spiel.getFeldCopilot("neigenFeld").setWuerfel(null);
        spiel.getFeldCopilot("speedFeld").setWuerfel(null);
        spiel.getFeldCopilot("radioFeld1").setWuerfel(null);
        spiel.getFeldCopilot("radioFeld2").setWuerfel(null);
    }



    void removeDiceFromOneUseFelds(ArrayList<Feld> oneUseFelds) {
        int i = 0;
        while (i < oneUseFelds.size()) {
            Feld f = oneUseFelds.get(i);
            if (f.isBelegt()) {
                f.clearOneUseFeld();
                oneUseFelds.remove(i);
            } else {
                i++;
            }
        }
    }

    ArrayList<Feld> getOneUseFelds(){
        ArrayList<Feld> oneUseFelds = new ArrayList<>();
        Feld fg12 = spiel.getFeldPilot("fahrgestellFeld1_2");
        Feld fg34 = spiel.getFeldPilot("fahrgestellFeld3_4");
        Feld fg56 = spiel.getFeldPilot("fahrgestellFeld5_6");
        Feld b2 = spiel.getFeldPilot("bremsenFeld2");
        Feld b4 = spiel.getFeldPilot("bremsenFeld4");
        Feld b6 = spiel.getFeldPilot("bremsenFeld6");

        Feld k12 = spiel.getFeldCopilot("klappenFeld1_2");
        Feld k23 = spiel.getFeldCopilot("klappenFeld2_3");
        Feld k45 = spiel.getFeldCopilot("klappenFeld4_5");
        Feld k56 = spiel.getFeldCopilot("klappenFeld5_6");

        oneUseFelds.add(fg12);
        oneUseFelds.add(fg34);
        oneUseFelds.add(fg56);
        oneUseFelds.add(b2);
        oneUseFelds.add(b4);
        oneUseFelds.add(b6);
        oneUseFelds.add(k12);
        oneUseFelds.add(k23);
        oneUseFelds.add(k45);
        oneUseFelds.add(k56);

        return oneUseFelds;
    }

    void clearKaffeFeld(){
        if (!spiel.getFeldPilot("kaffeFeld1").isBelegt()&&!spiel.getFeldCopilot("kaffeFeld1").isBelegt()){
            if (!spiel.getFeldPilot("kaffeFeld2").isBelegt()&&!spiel.getFeldCopilot("kaffeFeld2").isBelegt()){
                spiel.getFeldPilot("kaffeFeld3").setWuerfel(null);
                spiel.getFeldCopilot("kaffeFeld3").setWuerfel(null);
            }
            else {
                spiel.getFeldPilot("kaffeFeld2").setWuerfel(null);
                spiel.getFeldCopilot("kaffeFeld2").setWuerfel(null);
            }
        }
        else {
            spiel.getFeldPilot("kaffeFeld1").setWuerfel(null);
            spiel.getFeldCopilot("kaffeFeld1").setWuerfel(null);
        }
    }

}
