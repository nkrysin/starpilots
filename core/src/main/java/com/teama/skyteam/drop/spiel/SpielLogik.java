package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.gui.SchalterGUI;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spiel.action.factory.ActionFactory;
import com.teama.skyteam.drop.spiel.action.validator.ActionValidator;
import com.teama.skyteam.drop.spiel.action.validator.StandartActionValidator;
import com.teama.skyteam.drop.spiel.action.validator.ValidationResult;
import com.teama.skyteam.drop.spiel.action.type.ActionName;
import com.teama.skyteam.drop.spiel.gamestate.GameStateManager;
import com.teama.skyteam.drop.spiel.gamestate.StandardGameStateManager;
import com.teama.skyteam.drop.spiel.logik.GameRules;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.KaffeeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Die Klasse SpielLogik enthält die Hauptlogik und Steuerung des Spiels.
 * Sie definiert, wie Spieleraktionen verarbeitet werden und wie der Spielstatus aktualisiert wird.
 */

public class SpielLogik {
    private final Spiel spiel;
    private final Skin skin;
    private final Stage stage;
    private Window window;
    private SchalterGUI schalterGUI;
    private Image ruder;
    private int neuerWert;
    private SzenarioHoeheManager szenarioHoeheManager;
    private final GameRules gameRules;
    private final ActionValidator validator;
    private final ActionFactory actionFactory;
    private final GameStateManager gameStateManager;

    public SpielLogik(Spiel spiel, Stage stage, Skin skin, SchalterGUI schalterGUI, Image ruder, SzenarioHoeheManager szenarioHoeheManager, GameRules gameRules) {
        this.spiel = spiel;
        this.stage = stage;
        this.skin = skin;
        this.schalterGUI = schalterGUI;
        this.ruder = ruder;
        this.szenarioHoeheManager = szenarioHoeheManager;
        this.gameRules = gameRules;

        this.validator = new StandartActionValidator(); // TODO: Check this? Move to constr?
        this.gameStateManager = new StandardGameStateManager(spiel);
        this.actionFactory = new ActionFactory(spiel, gameStateManager);

    }

    /**
     * Überprüft, ob eine Ausfallbedingung im Spiel erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    boolean checkAusfallBedingung() {
        return gameRules.checkAusfallBedingung(spiel);
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    boolean checkFinalRound() {
        return gameRules.checkFinalRound(spiel);
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    boolean checkGewinnBedingung() {
        return gameRules.checkGewinnBedingung(spiel);
    }


    public boolean pilotSchritt(Spieler spieler, ActionName auswahl, Wuerfel gewaehlterWuerfel) {
        ValidationResult validation = validator.validatePilotAction(spiel, auswahl, gewaehlterWuerfel);
        if (validation.isValid()) {
            createMessage("Fehler", validation.getErrorMsg());
            return false;
        }

        Pilot pilot = (Pilot) spieler;
        PlayerAction action = actionFactory.createPilotAction(auswahl, pilot, gewaehlterWuerfel);
        ActionResult result = action.execute();

        if (!result.isValid()) {
            createMessage("Fehler", result.getErrorMsg());
            return false;
        }
        return true;
    }

    public boolean copilotSchritt(Spieler spieler, ActionName auswahl, Wuerfel gewaehlterWuerfel) {
        ValidationResult validation = validator.validateCopilotAction(spiel, auswahl, gewaehlterWuerfel);
        if (validation.isValid()) {
            createMessage("Fehler", validation.getErrorMsg());
            return false;
        }

        Copilot copilot = (Copilot) spieler;

        PlayerAction action = actionFactory.createCopilotAction(auswahl, copilot, gewaehlterWuerfel);
        ActionResult result = action.execute();

        if (!result.isValid()) {
            createMessage("Fehler", result.getErrorMsg());
            return false;
        }
        return true;
    }


    // Changed to updateTravelFieldState
    //boolean reiseFeldZustandAktualisieren() {};

    // Changed to handleRadio
    //void radio(Wuerfel gewaehlterWuerfel) {}

    // Changed to updateTilt
    //boolean neigenSetzen() {}

    // Changed to moveForward
    //void vorwaertsBewegen(int steps) {}


    //Changed to addNeuWurfTokens
    // void addNeuWurfTokens() {}


    // Changed to reduceAltitude
    //void hoeheReduzieren() {}

    //void setNeuWuerfels(Spieler spieler) {}

    //private ArrayList<Wuerfel> werfen() {}

    /**
     * Löscht die Würfel von den Feldern, die mehrmals in jeder Runde verwendet werden können.
     */
    void clearManyUseFelds() {
        spiel.getFeldPilot(FeldName.NEIGEN).setWuerfel(null);
        spiel.getFeldPilot(FeldName.SPEED).setWuerfel(null);
        spiel.getFeldPilot(FeldName.RADIO_PILOT).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.NEIGEN).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.SPEED).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.RADIO_COPILOT_1).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.RADIO_COPILOT_2).setWuerfel(null);
    }

    /**
     * Entfernt die Würfel von den Feldern, die nur einmal genutzt werden können.
     *
     * @param oneUseFelds Die Liste der Felder, die einmal genutzt werden können.
     */
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


    /**
     * Liefert eine Liste aller Felder, die nur einmal genutzt werden können.
     *
     * @return Eine Liste der Felder, die nur einmal genutzt werden können.
     */
    ArrayList<Feld> getOneUseFelds() {
        ArrayList<Feld> oneUseFelds = new ArrayList<>();
        Feld fg12 = spiel.getFeldPilot(FeldName.FAHRGESTELL_1_2);
        Feld fg34 = spiel.getFeldPilot(FeldName.FAHRGESTELL_3_4);
        Feld fg56 = spiel.getFeldPilot(FeldName.FAHRGESTELL_5_6);
        Feld b2 = spiel.getFeldPilot(FeldName.BREMSEN_2);
        Feld b4 = spiel.getFeldPilot(FeldName.BREMSEN_4);
        Feld b6 = spiel.getFeldPilot(FeldName.BREMSEN_6);
        Feld k12 = spiel.getFeldCopilot(FeldName.KLAPPEN_1_2);
        Feld k23 = spiel.getFeldCopilot(FeldName.KLAPPEN_2_3);
        Feld k45 = spiel.getFeldCopilot(FeldName.KLAPPEN_4_5);
        Feld k56 = spiel.getFeldCopilot(FeldName.KLAPPEN_5_6);
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

    /**
     * Entfernt die Würfel von den Kaffee-Feldern, basierend auf der Anzahl Kaffee-Tokens, die beide Spieler haben.
     */
    void clearKaffeeFeld() {
        int kaffeeCount = spiel.getKaffeeTokenCount();
        switch (kaffeeCount) {
            case 0:
                spiel.getFeldPilot(FeldName.KAFFEE_1).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_1).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
            case 1:
                spiel.getFeldPilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
            case 2:
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
        }

    }

    /**
     * Zeigt ein modales Fenster mit einer Nachricht an.
     *
     * @param title Der Titel des Fensters.
     * @param text  Der Inhalt der Nachricht.
     */
    public void createMessage(String title, String text) {
        if (window == null) {
            window = new Window(title, skin);
            window.setModal(true);
            window.setMovable(false);
            window.add(text).pad(20).row();
            TextButton closeButton = new TextButton("OK", skin);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    window.remove();
                    window = null;
                }
            });
            window.add(closeButton).pad(10).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein modales Fenster, um den Wert eines Würfels mit einem Kaffee-Token zu ändern.
     * Der Spieler kann den Wert des Würfels entweder erhöhen oder verringern, solange die Grenzen eingehalten werden.
     *
     * @param wuerfel  Der Würfel, dessen Wert geändert werden soll.
     * @param spieler  Der Spieler, der das Kaffee-Token verwendet.
     * @param index    Der Index des Würfels in der Würfelliste des Spielers.
     * @param callback Eine Callback-Funktion, die den neuen Wert des Würfels verarbeitet.
     */
    public void createKaffeeTokenWindow(Wuerfel wuerfel, Spieler spieler, int index, Consumer<Integer> callback) {
        if (window == null) {
            window = new Window("Wert aendern", skin);
            window.setModal(true);
            window.getTitleLabel().setAlignment(Align.center);

            window.add("Wie moechten Sie den Wert des Wuerfels aendern?").pad(20).center().row();

            Table buttonTable = new Table();
            buttonTable.center();

            TextButton plusButton = new TextButton("Plus", skin);
            plusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (wuerfel.getWert() < 6) {
                        spieler.kaffeeTokenBenutzen(kaffeeToken(), index, KaffeeTokenAktion.PLUS);
                        neuerWert = spieler.getWuerfels().get(index).getWert();
                        clearKaffeeFeld();
                        window.remove();
                        window = null;
                        callback.accept(neuerWert);
                    } else {
                        window.remove();
                        window = null;
                        createMessage("Fehler", "Das Würfel-Ergebnis kann nicht höher sein!");
                    }
                }
            });
            TextButton minusButton = new TextButton("Minus", skin);
            minusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (wuerfel.getWert() > 1) {
                        spieler.kaffeeTokenBenutzen(kaffeeToken(), index, KaffeeTokenAktion.MINUS);
                        neuerWert = spieler.getWuerfels().get(index).getWert();
                        clearKaffeeFeld();
                        window.remove();
                        window = null;
                        callback.accept(neuerWert);
                    } else {
                        window.remove();
                        window = null;
                        createMessage("Fehler", "Das Würfel-Ergebnis kann nicht niedriger sein!");
                    }
                }
            });
            buttonTable.add(plusButton).padRight(20);
            buttonTable.add(minusButton).padLeft(20);
            window.add(buttonTable).center().pad(20).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein modales Fenster, um ein NeuWurf-Token zu verwenden.
     * Der Spieler kann entscheiden, ob er neue Würfel einem Spieler oder beiden Spielern gleichzeitig gibt.
     *
     * @param spieler  Der Spieler, der das NeuWurf-Token verwendet.
     * @param callback Eine Callback-Funktion, die die Spieler aktualisiert, die neue Würfel erhalten haben.
     */
    public void createNeuWurfTokenWindow(Spieler spieler, BiConsumer<Spieler, Spieler> callback) {
        if (window == null) {
            window = new Window("Neu werfen", skin);
            window.setModal(true);
            window.setMovable(false);
            Label label = new Label("Wem möchten Sie neue Würfel geben?", skin);
            window.add(label).pad(20).row();
            Table buttonTable = new Table();
            NeuWurfToken token = neuWurfToken();
            Spieler pilot = spiel.getPilot();
            Spieler copilot = spiel.getCopilot();
            TextButton pilotButton = new TextButton("Pilot", skin);
            pilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    spieler.neuWurfTokenBenutzen(token, pilot);
                    window.remove();
                    window = null;
                    callback.accept(pilot, null);
                }
            });
            TextButton copilotButton = new TextButton("Copilot", skin);
            copilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    spieler.neuWurfTokenBenutzen(token, copilot);
                    window.remove();
                    window = null;
                    callback.accept(null, copilot);
                }
            });
            TextButton beideButton = new TextButton("Beide", skin);
            beideButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    spieler.neuWurfTokenBenutzen(token, pilot, copilot);
                    window.remove();
                    window = null;
                    callback.accept(pilot, copilot);
                }
            });
            buttonTable.add(pilotButton).pad(5);
            buttonTable.add(copilotButton).pad(5);
            buttonTable.add(beideButton).pad(5);
            window.add(buttonTable).center().pad(10).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein Fenster, das anzeigt, dass das Spiel gewonnen oder verloren wurde.
     *
     * @param win true, wenn das Spiel gewonnen wurde; false, wenn es verloren wurde,
     * das Bild im Fenster ist davon abhängig
     */
    public void createFinalWindow(boolean win) {
        if (window != null) {
            window.remove();
            window = null;
        }
        String path;
        String title;
        float w;
        float h;
        String buttonText;
        if (win) {
            path = TexturePaths.WIN_PATH;
            w = 800;
            h = 550;
            buttonText = "Hurra!";
            title = "Sieg!";
        } else {
            path = TexturePaths.LOOSE_PATH;
            w = 800;
            h = 450;
            buttonText = "Schade!";
            title = "Verloren!";
        }
        window = new Window(title, skin);
        window.setModal(true);
        Texture texture = new Texture(path);
        Image image = new Image(texture);
        window.add(image).pad(10).row();
        TextButton closeButton = new TextButton(buttonText, skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                window = null;
                Gdx.app.exit();
            }
        });
        window.add(closeButton).pad(10).row();
        window.setSize(w, h);
        window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
        stage.addActor(window);
    }
}
