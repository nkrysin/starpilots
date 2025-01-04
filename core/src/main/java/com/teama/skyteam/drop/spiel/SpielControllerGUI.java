package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.gui.GameUIManager;
import com.teama.skyteam.drop.gui.SpielGUI;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;

import java.util.ArrayList;

public class SpielControllerGUI {

    private final Spiel spiel;
    private final SpielLogik spielLogik;
    private final GameUIManager gameUIManager;
    private final SpielGUI spielGUI;
    boolean nowMustBeFirst = true;
    boolean nowMustBeSecond = false;
    boolean coffeeUsed = false;
    private State state = State.CHECK_END;
    private Spieler ersterSpieler;
    private Spieler zweiterSpieler;
    private boolean neigungErledigt = false;
    private boolean reiseFeldAktualisiert = false;
    private boolean letzteRunde = false;
    private boolean spielEnde = false;

    public SpielControllerGUI(Spiel spiel, GameUIManager gameUIManager, Stage stage, Skin skin) {
        this.spiel = spiel;
        this.spielGUI = new SpielGUI(spiel, stage, skin, gameUIManager);
        this.gameUIManager = gameUIManager;
        this.spielLogik = new SpielLogik(spiel, stage, skin, spielGUI.getSchalterGUI(), spielGUI.getRuder(), gameUIManager);

        this.spielGUI.setPlayerActionCallback(new PlayerActionCallback() {
            @Override
            public void onPlayerActionCompleted(Spieler spieler) {
                // ЧИСТИМ только у того, кто закончил ход
                spielGUI.clearPlayerListeners(spieler);
                spielenSchritt();
            }
        });
        spielenSchritt();
    }

    public void spielenSchritt() {
        if (spielEnde) {
            return;
        }
        while (!spielEnde) {
            switch (state) {
                case CHECK_END:
                    if (spielLogik.checkAusfallBedingung()) {
                        spielEnde = true;
                        state = State.END;
                        break;
                    } else {
                        if (!letzteRunde && spielLogik.checkFinalRound()) {
                            letzteRunde = true;
                        }
                        state = State.PREPARE_ROUND;
                    }
                    break;

                case PREPARE_ROUND:
                    prepareRound();
                    state = State.PLAY_TURN_FIRST;
                    break;

                case PLAY_TURN_FIRST:
                    if (ersterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung() && nowMustBeFirst) {
                        spielGUI.addWuerfelListenersForSpieler(ersterSpieler);
                        spielGUI.addFieldListenersForPlacement(ersterSpieler);
                        spielGUI.checkFeldSituation(ersterSpieler);
                        spielGUI.setKoffeeGroupsActive(ersterSpieler);

                        if (ersterSpieler instanceof Pilot) {
                            spielGUI.showCoffee(true, false);
                        } else {
                            spielGUI.showCoffee(false, true);
                        }
                        spielGUI.showNeuWurf(true);

                        spielGUI.useCoffee(ersterSpieler);
                        spielGUI.useNeuWurf(ersterSpieler);

                        nowMustBeFirst = false;
                        nowMustBeSecond = true;
                        return;
                    } else {
                        state = State.CHECK_NEIGUNG;
                    }
                    break;

                case CHECK_NEIGUNG:
                    if (!neigungErledigt && spielLogik.neigenSetzen()) {
                        neigungErledigt = true;
                    }
                    state = State.CHECK_REISEFELD;
                    break;

                case CHECK_REISEFELD:
                    if (!reiseFeldAktualisiert && spielLogik.reiseFeldZustandAktualisieren()) {
                        reiseFeldAktualisiert = true;
                    }
                    state = State.PLAY_TURN_SECOND;
                    break;

                case PLAY_TURN_SECOND:
                    if (zweiterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung() && nowMustBeSecond) {
                        spielGUI.addWuerfelListenersForSpieler(zweiterSpieler);
                        spielGUI.addFieldListenersForPlacement(zweiterSpieler);
                        spielGUI.checkFeldSituation(zweiterSpieler);
                        spielGUI.setKoffeeGroupsActive(zweiterSpieler);

                        if (zweiterSpieler instanceof Pilot) {
                            spielGUI.showCoffee(true, false);
                        } else {
                            spielGUI.showCoffee(false, true);
                        }
                        spielGUI.showNeuWurf(true);
                        spielGUI.useNeuWurf(zweiterSpieler);
                        spielGUI.useCoffee(zweiterSpieler);

                        nowMustBeFirst = true;
                        nowMustBeSecond = false;
                        return;
                    } else {
                        state = State.CHECK_NEIGUNG_AGAIN;
                    }
                    break;

                case CHECK_NEIGUNG_AGAIN:
                    if (!neigungErledigt && spielLogik.neigenSetzen()) {
                        neigungErledigt = true;
                    }
                    state = State.CHECK_REISEFELD_AGAIN;
                    break;

                case CHECK_REISEFELD_AGAIN:
                    if (!reiseFeldAktualisiert && spielLogik.reiseFeldZustandAktualisieren()) {
                        reiseFeldAktualisiert = true;
                    }
                    if (!spielLogik.checkAusfallBedingung() &&
                        (ersterSpieler.getAnzahlWuerfels() > 0 || zweiterSpieler.getAnzahlWuerfels() > 0)) {
                        state = State.PLAY_TURN_FIRST;
                    } else {
                        state = State.ROUND_FINISH;
                    }
                    break;

                case ROUND_FINISH:
                    finishRound();
                    if (spielLogik.checkAusfallBedingung()) {
                        spielEnde = true;
                        spielLogik.createFinalWindow(false);
                        state = State.END;
                    } else if (letzteRunde) {
                        state = State.FINAL_CHECK;
                    } else {
                        state = State.CHECK_END;
                    }
                    break;

                case FINAL_CHECK:
                    if (spielLogik.checkGewinnBedingung()) {
                        spielLogik.createFinalWindow(true);
                    } else {
                        spielLogik.createFinalWindow(false);
                    }
                    spielEnde = true;
                    state = State.END;
                    break;

                case END:
                    spielLogik.createFinalWindow(false);
                    spielEnde = true;
                    break;
            }
            if (spielEnde) {
                break;
            }
        }
    }

    private void prepareRound() {
        neigungErledigt = false;
        reiseFeldAktualisiert = false;

        spielLogik.addNeuWurfTokens();
        boolean isEvenRound = (spiel.getRunde() + 1) % 2 == 0;
        ersterSpieler = isEvenRound ? spiel.getPilot() : spiel.getCopilot();
        zweiterSpieler = isEvenRound ? spiel.getCopilot() : spiel.getPilot();

        spielLogik.setNeuWuerfels(ersterSpieler);
        spielLogik.setNeuWuerfels(zweiterSpieler);

        spielGUI.createWuerfelForSpieler(ersterSpieler);
        spielGUI.createWuerfelForSpieler(zweiterSpieler);
    }

    private void finishRound() {
        ArrayList<Feld> oneUseFelds = spielLogik.getOneUseFelds();
        spielLogik.removeDiceFromOneUseFelds(oneUseFelds);
        spielLogik.clearManyUseFelds();
        spielGUI.clearManyUseFields();
        gameUIManager.updateHoehe();

        spiel.setRunde(spiel.getRunde() + 1);
        spielLogik.hoeheReduzieren();
        spiel.setEndBedingung(spielLogik.checkAusfallBedingung());
    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void render() {
        spielGUI.stage.act(Gdx.graphics.getDeltaTime());
        spielGUI.stage.draw();
    }

    public void resize(int width, int height) {
        spielGUI.stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        spielGUI.stage.dispose();
    }

    private enum State {
        CHECK_END,
        PREPARE_ROUND,
        PLAY_TURN_FIRST,
        CHECK_NEIGUNG,
        CHECK_REISEFELD,
        PLAY_TURN_SECOND,
        CHECK_NEIGUNG_AGAIN,
        CHECK_REISEFELD_AGAIN,
        ROUND_FINISH,
        FINAL_CHECK,
        END
    }
}
