package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.gui.GameUIManager;
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

    private State state = State.CHECK_END;

    private Spieler ersterSpieler;
    private Spieler zweiterSpieler;
    private boolean neigungErledigt = false;
    private boolean reiseFeldAktualisiert = false;
    private boolean letzteRunde = false;
    private boolean spielEnde = false;

    public SpielControllerGUI(Spiel spiel, SpielLogik spielLogik, GameUIManager gameUIManager) {
        this.spiel = spiel;
        this.spielLogik = spielLogik;
        this.gameUIManager = gameUIManager;
    }

    public void spielenSchritt() {
        if (spielEnde) {
            return;
        }

        switch (state) {
            case CHECK_END:
                if (spielLogik.checkEndBedingung()) {
                    spielEnde = true;
                    state = State.END;
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
                if (ersterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung()) {
                    einenZugMachen(ersterSpieler);
                }
                state = State.CHECK_NEIGUNG;
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
                if (zweiterSpieler.getAnzahlWuerfels() > 0 && !spielLogik.checkAusfallBedingung()) {
                    einenZugMachen(zweiterSpieler);
                }
                state = State.CHECK_NEIGUNG_AGAIN;
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
                // повтор хода, если у игроков есть кубики
                if (!spielLogik.checkAusfallBedingung() &&
                    (ersterSpieler.getAnzahlWuerfels() > 0 || zweiterSpieler.getAnzahlWuerfels() > 0)) {
                    // начинаем заново с хода первого игрока
                    state = State.PLAY_TURN_FIRST;
                } else {
                    state = State.ROUND_FINISH;
                }
                break;

            case ROUND_FINISH:
                finishRound();
                if (spielLogik.checkAusfallBedingung()) {
                    // проигрыш
                    spielEnde = true;
                    state = State.END;
                } else if (letzteRunde) {
                    state = State.FINAL_CHECK;
                } else {
                    state = State.CHECK_END;
                }
                break;

            case FINAL_CHECK:
                if (spielLogik.checkGewinnBedingung()) {
                    // TODO: окно победы
                } else {
                    // TODO: окно проигрыша
                }
                spielEnde = true;
                state = State.END;
                break;

            case END:
                break;
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
    }

    private void finishRound() {
        ArrayList<Feld> oneUseFelds = spielLogik.getOneUseFelds();
        spielLogik.removeDiceFromOneUseFelds(oneUseFelds);
        spielLogik.clearManyUseFelds();
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
}
