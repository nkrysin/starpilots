package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.gui.SpielGUI;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;

import java.util.ArrayList;

public class SpielControllerGUI {

    private final Spiel spiel;
    private final SpielLogik spielLogik;
    private final SzenarioHoeheManager szenarioHoeheManager;
    private final SpielGUI spielGUI;
    boolean nowMustBeFirst = true;
    boolean nowMustBeSecond = false;
    private State state = State.CHECK_END;
    private Spieler ersterSpieler;
    private Spieler zweiterSpieler;
    private boolean neigungErledigt = false;
    private boolean reiseFeldAktualisiert = false;
    private boolean letzteRunde = false;
    private boolean spielEnde = false;

    public SpielControllerGUI(Spiel spiel, SzenarioHoeheManager szenarioHoeheManager, Stage stage, Skin skin) {
        this.spiel = spiel;
        this.spielGUI = new SpielGUI(spiel, stage, skin, szenarioHoeheManager);
        this.szenarioHoeheManager = szenarioHoeheManager;
        this.spielLogik = new SpielLogik(spiel, stage, skin, spielGUI.getSchalterGUI(), spielGUI.getRuder(), szenarioHoeheManager);

        this.spielGUI.setPlayerActionCallback(new PlayerActionCallback() {
            @Override
            public void onPlayerActionCompleted(Spieler spieler) {
                spielGUI.clearPlayerListeners(spieler);
                spielenSchritt();
            }
        });
        spielenSchritt();
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
        szenarioHoeheManager.updateHoehe();

        spiel.setRunde(spiel.getRunde() + 1);
        spielLogik.hoeheReduzieren();
        spiel.setEndBedingung(spielLogik.checkAusfallBedingung());
        spielGUI.removeAllHideImages();
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

                        spielGUI.hideWuerfel(ersterSpieler);
                        spielGUI.addWuerfelListenersForSpieler(ersterSpieler);
                        spielGUI.addFieldListenersForPlacement(ersterSpieler);
                        spielGUI.checkFeldSituation(ersterSpieler);
                        spielGUI.setKaffeeGroupsActive(ersterSpieler);

                        if (ersterSpieler instanceof Pilot) {
                            spielGUI.showKaffee(true, false);
                        } else {
                            spielGUI.showKaffee(false, true);
                        }
                        spielGUI.showNeuWurf(true);

                        spielGUI.useKaffee(ersterSpieler);

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

                        spielGUI.hideWuerfel(zweiterSpieler);
                        spielGUI.addWuerfelListenersForSpieler(zweiterSpieler);
                        spielGUI.addFieldListenersForPlacement(zweiterSpieler);
                        spielGUI.checkFeldSituation(zweiterSpieler);
                        spielGUI.setKaffeeGroupsActive(zweiterSpieler);

                        if (zweiterSpieler instanceof Pilot) {
                            spielGUI.showKaffee(true, false);
                        } else {
                            spielGUI.showKaffee(false, true);
                        }
                        spielGUI.showNeuWurf(true);
                        spielGUI.useNeuWurf(zweiterSpieler);
                        spielGUI.useKaffee(zweiterSpieler);

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

    public void render() {
        spielGUI.stage.act(Gdx.graphics.getDeltaTime());
        spielGUI.stage.draw();
    }

    public void resize(int width, int height) {
        spielGUI.stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        if (spielGUI != null) {
            spielGUI.dispose();
        }
    }
}
