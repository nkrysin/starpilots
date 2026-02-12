package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.teama.skyteam.drop.spiel.action.validator.StandardActionValidator;
import com.teama.skyteam.drop.spiel.action.validator.ValidationResult;
import com.teama.skyteam.drop.spiel.action.type.ActionName;
import com.teama.skyteam.drop.spiel.gamestate.GameStateManager;
import com.teama.skyteam.drop.spiel.gamestate.StandardGameStateManager;
import com.teama.skyteam.drop.spiel.logik.rules.GameRules;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.KaffeeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
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

        this.validator = new StandardActionValidator(); // TODO: Check this? Move to constr?
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


    // Changed/Moved to StandardGameStateManager/updateTravelFieldState
    //boolean reiseFeldZustandAktualisieren() {};

    // Changed/Moved to StandardGameStateManager/handleRadio
    //void radio(Wuerfel gewaehlterWuerfel) {}

    // Changed/Moved to StandardGameStateManager/updateTilt
    //boolean neigenSetzen() {}

    // Changed/Moved to StandardGameStateManager/moveForward
    //void vorwaertsBewegen(int steps) {}


    //Changed/Moved to StandardGameStateManager/addNeuWurfTokens
    // void addNeuWurfTokens() {}


    // Changed/Moved to StandardGameStateManager/reduceAltitude
    //void hoeheReduzieren() {}

    //void setNeuWuerfels(Spieler spieler) {}

    //private ArrayList<Wuerfel> werfen() {}



    // Changed/Moved to StandardFieldManager
    //void clearManyUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void removeDiceFromOneUseFelds(ArrayList<Feld> oneUseFelds) {}


    // Changed/Moved to StandardFieldManager
    //ArrayList<Feld> getOneUseFelds() {}

    // Changed/Moved to StandardFieldManager
    //void clearKaffeeFeld() { }



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
