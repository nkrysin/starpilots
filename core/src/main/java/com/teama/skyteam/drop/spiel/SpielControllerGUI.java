package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.gui.GameUIManager;
import com.teama.skyteam.drop.gui.SpielGUI;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Spieler;

import java.util.ArrayList;

public class SpielControllerGUI {

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

    private final Spiel spiel;
    private final SpielLogik spielLogik;
    private final GameUIManager gameUIManager;
    private final SpielGUI spielGUI;

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
                // очищаем слушателей после завершения хода
                spielGUI.clearPlayerListeners(spieler);
                // переходим к следующему состоянию игры
                spielenSchritt();
            }
        });

        // Начинаем первый шаг игры
        spielenSchritt();
    }

    boolean nowMustBeFirst = true;
    boolean nowMustBeSecond = false;

    public void spielenSchritt() {
        //TODO Remove this
        //System.out.println("spielenSchritt aufgerufen. spielEnde: " + spielEnde + ", state: " + state);
        if (spielEnde) {
            //TODO Remove this
            //System.out.println("Spiel ist beendet. Spielschritt wird nicht fortgesetzt.");
            return;
        }

        while (!spielEnde) {
            switch (state) {
                case CHECK_END:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("CHECK_END");
                    if (spielLogik.checkAusfallBedingung()) { // fixed here
                        spielEnde = true;
                        state = State.END;
                        printState(state);
                        break;
                    } else {
                        if (!letzteRunde && spielLogik.checkFinalRound()) {
                            letzteRunde = true;
                        }
                        state = State.PREPARE_ROUND;
                    }
                    break;

                case PREPARE_ROUND:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("PREPARE_ROUND");
                    prepareRound();
                    state = State.PLAY_TURN_FIRST;
                    break;

                case PLAY_TURN_FIRST:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("PLAY_TURN_FIRST");
                    if (ersterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung() && nowMustBeFirst) {
                        spielGUI.addWuerfelListenersForSpieler(ersterSpieler);
                        spielGUI.addFieldListenersForPlacement(ersterSpieler);
                        spielGUI.checkFeldSituation(ersterSpieler);
                        spielGUI.showCoffee(true);
                        spielGUI.showNeuWurf(true);
                        einenZugMachen(ersterSpieler);
                        nowMustBeFirst = false;
                        nowMustBeSecond = true;
                        // ожидаем завершения хода через callback
                        return;
                    } else {
                        state = State.CHECK_NEIGUNG;
                    }
                    break;

                case CHECK_NEIGUNG:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("CHECK_NEIGUNG");
                    if (!neigungErledigt && spielLogik.neigenSetzen()) {
                        neigungErledigt = true;
                    }
                    state = State.CHECK_REISEFELD;
                    break;

                case CHECK_REISEFELD:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("CHECK_REISEFELD");
                    if (!reiseFeldAktualisiert && spielLogik.reiseFeldZustandAktualisieren()) {
                        reiseFeldAktualisiert = true;
                    }
                    state = State.PLAY_TURN_SECOND;
                    break;

                case PLAY_TURN_SECOND:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("PLAY_TURN_SECOND");
                    if (zweiterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung() && nowMustBeSecond) {
                        spielGUI.addWuerfelListenersForSpieler(zweiterSpieler);
                        spielGUI.addFieldListenersForPlacement(zweiterSpieler);
                        spielGUI.checkFeldSituation(zweiterSpieler);
                        spielGUI.showCoffee(true);
                        spielGUI.showNeuWurf(true);
                        einenZugMachen(zweiterSpieler);
                        nowMustBeFirst = true;
                        nowMustBeSecond = false;
                        return;
                    } else {
                        state = State.CHECK_NEIGUNG_AGAIN;
                    }
                    break;

                case CHECK_NEIGUNG_AGAIN:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("CHECK_NEIGUNG_AGAIN");
                    if (!neigungErledigt && spielLogik.neigenSetzen()) {
                        neigungErledigt = true;
                    }
                    state = State.CHECK_REISEFELD_AGAIN;
                    break;

                case CHECK_REISEFELD_AGAIN:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("CHECK_REISEFELD_AGAIN");
                    if (!reiseFeldAktualisiert && spielLogik.reiseFeldZustandAktualisieren()) {
                        reiseFeldAktualisiert = true;
                    }
                    // повтор хода, если у игроков есть кубики
                    if (!spielLogik.checkAusfallBedingung() &&
                        (ersterSpieler.getAnzahlWuerfels() > 0 || zweiterSpieler.getAnzahlWuerfels() > 0)) {
                        // начинаем заново с хода первого игрока
                        state = State.PLAY_TURN_FIRST;
                    } else {
                        state = State.ROUND_FINISH;
                    }
                    printState(state);
                    break;

                case ROUND_FINISH:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("ROUND_FINISH");
                    finishRound();
                    if (spielLogik.checkAusfallBedingung()) {
                        // проигрыш
                        spielEnde = true;
                        state = State.END;
                        printState(state);
                    } else if (letzteRunde) {
                        state = State.FINAL_CHECK;
                    } else {
                        state = State.CHECK_END;
                    }
                    break;

                case FINAL_CHECK:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("FINAL_CHECK");
                    if (spielLogik.checkGewinnBedingung()) {
                        // TODO: окно победы
                        //System.out.println("Spieler hat gewonnen!");
                    } else {
                        // TODO: окно проигрыша
                        //System.out.println("Spieler hat verloren!");
                    }
                    spielEnde = true;
                    state = State.END;
                    break;

                case END:
                    //TODO Remove this
                    printState(state);
                    //System.out.println("END");


                    spielEnde = true;
                    break;
            }

            if (spielEnde) {
                //TODO Remove this
                //System.out.println("Spiel ist beendet");
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

    private void einenZugMachen(Spieler spieler) {
        // TODO: реализовать как-то выбор того, что хочет игрок
        int action = 1;
        switch (action) {
            case 1:
                // TODO: Проверяет ситуацию на поле, взывает verpflicteteAktion
                //spielTextGUI.checkFeldSituation(spieler);
                break;
            case 2:
                // TODO: KaffeeToken + вывод ситуации поля
                //spielTextGUI.kaffeToken(spieler);
                //spielTextGUI.checkFeldSituation(spieler);
                break;
            case 3:
                // TODO: NeuWurfToken + вывод ситуации поля
                //spielTextGUI.neuWurfToken(spieler);
                //spielTextGUI.checkFeldSituation(spieler);
                break;
            default:
                // TODO: Окно Dialog, которое выводит "неверное действие"
                break;
        }

    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void render() {
        spielGUI.stage.act(Gdx.graphics.getDeltaTime());
        spielGUI.stage.draw();
        spielGUI.stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ////System.out.println("Clicks coordinates: X = " + x + ", Y = " + y);
                return true;
            }
        });
    }
    public void resize(int width, int height) {
        spielGUI.stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        spielGUI.stage.dispose();
    }

    public void printState(State state) {
        switch (state) {
            case CHECK_END:
                //System.out.println("Current state: CHECK_END");
                break;
            case PREPARE_ROUND:
                //System.out.println("Current state: PREPARE_ROUND");
                break;
            case PLAY_TURN_FIRST:
                //System.out.println("Current state: PLAY_TURN_FIRST");
                break;
            case CHECK_NEIGUNG:
                //System.out.println("Current state: CHECK_NEIGUNG");
                break;
            case CHECK_REISEFELD:
                //System.out.println("Current state: CHECK_REISEFELD");
                break;
            case PLAY_TURN_SECOND:
                //System.out.println("Current state: PLAY_TURN_SECOND");
                break;
            case CHECK_NEIGUNG_AGAIN:
                //System.out.println("Current state: CHECK_NEIGUNG_AGAIN");
                break;
            case CHECK_REISEFELD_AGAIN:
                //System.out.println("Current state: CHECK_REISEFELD_AGAIN");
                break;
            case ROUND_FINISH:
                //System.out.println("Current state: ROUND_FINISH");
                break;
            case FINAL_CHECK:
                //System.out.println("Current state: FINAL_CHECK");
                break;
            case END:
                //System.out.println("Current state: END");
                break;
            default:
                //System.out.println("Unknown state");
                break;
        }
    }


}
