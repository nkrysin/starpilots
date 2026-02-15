package com.teama.skyteam.drop.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeTokenAction;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;
import com.teama.skyteam.drop.ui.constants.TexturePaths;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DialogManager {

    private Window window;
    private final Skin skin;
    private final Stage stage;
    private final Game game;

    public DialogManager(Skin skin, Stage stage, Game game) {
        this.skin = skin;
        this.stage = stage;
        this.game = game;
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
     * Der Player kann den Wert des Würfels entweder erhöhen oder verringern, solange die Grenzen eingehalten werden.
     *
     * @param dice  Der Würfel, dessen Wert geändert werden soll.
     * @param player  Der Player, der das Kaffee-Token verwendet.
     * @param index    Der Index des Würfels in der Würfelliste des Spielers.
     * @param callback Eine Callback-Funktion, die den neuen Wert des Würfels verarbeitet.
     */
    public void creatCoffeeTokenWindow(Dice dice, Player player, int index, GameStateManager gameStateManager, FieldManager fieldManager,
                                       Consumer<Integer> callback) {
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
                    if (dice.getValue() < 6) {
                        player.useCoffeToken(gameStateManager.takeCoffeeToken(), CoffeeTokenAction.PLUS, dice);
                        int neuerWert = player.getDice(dice).getValue();
                        fieldManager.clearCoffeeFields(game, game.getCoffeeTokenCount());
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
                    if (dice.getValue() > 1) {
                        player.useCoffeToken(gameStateManager.takeCoffeeToken(), CoffeeTokenAction.MINUS, dice);
                        int neuerWert = player.getAllDice().get(index).getValue();
                        fieldManager.clearCoffeeFields(game, game.getCoffeeTokenCount());
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
     * Der Player kann entscheiden, ob er neue Würfel einem Player oder beiden Spielern gleichzeitig gibt.
     *
     * @param player  Der Player, der das NeuWurf-Token verwendet.
     * @param callback Eine Callback-Funktion, die die Player aktualisiert, die neue Würfel erhalten haben.
     */
    public void createRerollTokenWindow(Player player, GameStateManager gameStateManager, BiConsumer<Player, Player> callback) {
        if (window == null) {
            window = new Window("Neu createRandomDice", skin);
            window.setModal(true);
            window.setMovable(false);
            Label label = new Label("Wem möchten Sie neue Würfel geben?", skin);
            window.add(label).pad(20).row();
            Table buttonTable = new Table();
            RerollToken token = gameStateManager.takeRerollToken();
            Player pilot = game.getPilot();
            Player copilot = game.getCopilot();
            TextButton pilotButton = new TextButton("Pilot", skin);
            pilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, pilot);
                    window.remove();
                    window = null;
                    callback.accept(pilot, null);
                }
            });
            TextButton copilotButton = new TextButton("Copilot", skin);
            copilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, copilot);
                    window.remove();
                    window = null;
                    callback.accept(null, copilot);
                }
            });
            TextButton beideButton = new TextButton("Beide", skin);
            beideButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, pilot, copilot);
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
     * Erstellt ein Fenster, das anzeigt, dass das Game gewonnen oder verloren wurde.
     *
     * @param win true, wenn das Game gewonnen wurde; false, wenn es verloren wurde,
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
