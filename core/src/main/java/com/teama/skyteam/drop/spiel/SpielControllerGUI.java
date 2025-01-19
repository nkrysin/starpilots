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
/**
 * Die Klasse SpielControllerGUI steuert den Spielfluss und die Interaktion mit der Benutzeroberfläche.
 * Sie verwendet die Zustandsmaschine, um den Spielverlauf zu organisieren und zwischen verschiedenen Spielphasen zu wechseln.
 */
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

        // Setzt die Callback-Funktion für Spieleraktionen.
        this.spielGUI.setPlayerActionCallback(new PlayerActionCallback() {
            @Override
            public void onPlayerActionCompleted(Spieler spieler) {
                spielGUI.clearPlayerListeners(spieler);
                spielenSchritt();
            }
        });
        spielenSchritt();
    }


    /**
     * Aufzählung von Zuständen für die Zustandsmaschine des Spiels.
     */
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

    /**
     * Bereitet eine neue Runde vor, indem die Spieler und ihre Würfel initialisiert werden.
     * Wird auch berechnet, wer einen ersten Zug in der Runde macht
     */
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

    /**
     * Beendet die aktuelle Runde, aktualisiert die Spielfelder und prüft auf Ausfallbedingungen.
     */
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

    /**
     * Die Methode spielenSchritt steuert den Spielfluss basierend auf einer Zustandsmaschine.
     * Sie wechselt zwischen den verschiedenen Zuständen des Spiels und verwaltet die Aktionen der Spieler,
     * die Prüfung von Spielfeldern und das Ende des Spiels.
     *
     * Der Ablauf wird in mehreren Zuständen organisiert:
     *
     * 1. CHECK_END: Prüft, ob eine Ausfallbedingung erreicht wurde.
     *    Wenn ja, endet das Spiel. Ansonsten wird zur Vorbereitung der nächsten Runde gewechselt.
     *
     * 2. PREPARE_ROUND: Initialisiert die Runde, setzt Würfel und Spieler für die aktuelle Runde.
     *
     * 3. PLAY_TURN_FIRST: Führt die Aktionen des ersten Spielers aus.
     *    Der Spieler kann Würfel auf Feldern platzieren, spezielle Aktionen wie Kaffee- oder NeuWurf-Tokens ausführen.
     *    Nach dem Zug des ersten Spielers wird die Neigung geprüft.
     *
     * 4. CHECK_NEIGUNG: Überprüft und aktualisiert die Neigung des Flugzeugs, falls erforderlich.
     *    Anschließend wird der Zustand des Spielfelds geprüft.
     *
     * 5. CHECK_REISEFELD: Aktualisiert den Zustand des aktuellen Reisefelds basierend auf der Geschwindigkeit.
     *    Danach wird der Zug des zweiten Spielers eingeleitet.
     *
     * 6. PLAY_TURN_SECOND: Führt die Aktionen des zweiten Spielers aus.
     *    Analog zum ersten Spieler können Würfel platziert und spezielle Aktionen ausgeführt werden.
     *    Nach dem Zug des zweiten Spielers wird die Neigung erneut geprüft.
     *
     * 7. CHECK_NEIGUNG_AGAIN: Prüft die Neigung erneut nach dem Zug des zweiten Spielers.
     *    Danach wird der Zustand des Spielfelds erneut überprüft.
     *
     * 8. CHECK_REISEFELD_AGAIN: Führt eine erneute Prüfung des Spielfelds durch.
     *    Falls noch Würfel übrig sind, wird der Zug des ersten Spielers wiederholt.
     *    Andernfalls wird die Runde beendet.
     *
     * 9. ROUND_FINISH: Beendet die Runde, bereinigt Spielfelder und prüft erneut auf Ausfallbedingungen.
     *    Wenn die letzte Runde erreicht wurde, wird zur finalen Überprüfung gewechselt.
     *
     * 10. FINAL_CHECK: Prüft, ob die Gewinnbedingungen erfüllt sind.
     *     Zeigt ein finales Fenster an, das entweder den Sieg oder die Niederlage signalisiert.
     *
     * 11. END: Zeigt das Endfenster an und beendet das Spiel.
     *
     * Die Methode verwendet eine Schleife, um den aktuellen Zustand kontinuierlich zu überprüfen und in den nächsten zu wechseln.
     * Die Zustände definieren dabei klar die logische Abfolge der Spielaktionen.
     */
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
                        spielGUI.removeAllHideImages();
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
                        spielGUI.removeAllHideImages();
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

    /**
     * Zeichnet die GUI-Komponenten und führt Aktionen aus.
     */
    public void render() {
        spielGUI.stage.act(Gdx.graphics.getDeltaTime());
        spielGUI.stage.draw();
    }

    /**
     * Aktualisiert die Größenanpassung der GUI bei Änderung der Fenstergröße.
     *
     * @param width  Neue Breite des Fensters.
     * @param height Neue Höhe des Fensters.
     */
    public void resize(int width, int height) {
        spielGUI.stage.getViewport().update(width, height, true);
    }

    /**
     * Gibt alle Ressourcen der GUI frei.
     */
    public void dispose() {
        if (spielGUI != null) {
            spielGUI.dispose();
        }
    }
}
