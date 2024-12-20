package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.gui.GameUIManager;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Spieler;

import java.util.ArrayList;



public class SpielController {

    private final Spiel spiel;
    private final SpielLogik spielLogik;
    //TODO изменить наормальный GUI
    private final SpielTextGUI spielTextGUI;
    private final GameUIManager gameUIManager;

    public SpielController(Spiel spiel, GameUIManager gameUIManager) {
        this.spiel = spiel;
        this.spielLogik = new SpielLogik(spiel);
        //TODO изменить наормальный GUI
        this.spielTextGUI = new SpielTextGUI(spiel);
        this.gameUIManager = gameUIManager;
    }

    //Die Methode entscheidet je nach Runde, welcher Spieler zuerst am Zug ist.
    //Außerdem können sich die Spieler während der Runde abwechseln.
    public void spielen() {

        while (!spielLogik.checkEndBedingung()) {
            spielLogik.addNeuWurfTokens();

            ArrayList<Feld> oneUseFelds = spielLogik.getOneUseFelds();

            spielRunde();

            spielLogik.removeDiceFromOneUseFelds(oneUseFelds);
            spielLogik.clearManyUseFelds();
            spiel.setRunde(spiel.getRunde() + 1);
            spielLogik.hoeheReduzieren();
            spiel.setEndBedingung(spielLogik.checkAusfallBedingung()); //changed to Ausfall


            if (spielLogik.checkFinalRound()){
                spielRunde();
                if (spielLogik.checkGewinnBedingung()) {

                    //TODO Окно Dialog, чтобы показывать победное окно
                } else {
                    //TODO Окно Dialog, чтобы показывать проигрышное окно
                }
                break;
            }
        }

        if(spielLogik.checkAusfallBedingung()){
            //TODO Окно Dialog, чтобы показывать проигрышное окно
        }



    }


    private void spielRunde(){
        boolean isEvenRound = (spiel.getRunde() + 1) % 2 == 0;
        Spieler ersterSpieler = isEvenRound ? spiel.getPilot() : spiel.getCopilot();
        Spieler zweiterSpieler = isEvenRound ? spiel.getCopilot() : spiel.getPilot();

        boolean neigungErledigt = false;
        boolean reiseFeldAktualisiert = false;
        spielLogik.setNeuWuerfels(ersterSpieler);
        spielLogik.setNeuWuerfels(zweiterSpieler);

        while (!spielLogik.checkAusfallBedingung() && (ersterSpieler.getAnzahlWuerfels() > 0 || zweiterSpieler.getAnzahlWuerfels() > 0)) {
            if (ersterSpieler.getAnzahlWuerfels() > 0) {
                if (ersterSpieler.equals(spiel.getPilot())) {
                    //ход пилота
                    spielTextGUI.printWuerfel(spiel.getPilot());
                    einenZugMachen(ersterSpieler);
                } else {
                    //ход сопилота
                    spielTextGUI.printWuerfel(spiel.getCopilot());
                    einenZugMachen(ersterSpieler);
                }

            }

            if (!neigungErledigt){
                if(spielLogik.neigenSetzen()){
                    neigungErledigt = true;
                }
            }

            if (!reiseFeldAktualisiert){
                if(spielLogik.reiseFeldZustandAktualisieren()){
                    reiseFeldAktualisiert = true;
                }
            }

            if (zweiterSpieler.getAnzahlWuerfels() > 0) {
                if (zweiterSpieler.equals(spiel.getPilot())) {
                    //ход пилота
                    spielTextGUI.printWuerfel(spiel.getPilot());
                } else {
                    //ход сопилота
                    spielTextGUI.printWuerfel(spiel.getCopilot());
                }
                einenZugMachen(zweiterSpieler);
            }
            if (!neigungErledigt){
                if(spielLogik.neigenSetzen()){
                    neigungErledigt = true;
                }
            }
            if (!reiseFeldAktualisiert){
                if(spielLogik.reiseFeldZustandAktualisieren()){
                    reiseFeldAktualisiert = true;
                }
            }
        }
    }

    private void einenZugMachen(Spieler spieler) {
        int action = spielTextGUI.actionWaehlen();
        switch (action) {
            case 1:
                spielTextGUI.checkFeldSituation(spieler);
                break;
            case 2:
                spielTextGUI.kaffeToken(spieler);
                spielTextGUI.checkFeldSituation(spieler);
                break;
            case 3:
                spielTextGUI.neuWurfToken(spieler);
                spielTextGUI.checkFeldSituation(spieler);
                break;
            default:
                //TODO Окно Dialog, которое выводит "неверное действие"
                break;
        }
    }

}
