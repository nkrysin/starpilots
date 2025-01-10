package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teama.skyteam.drop.flugzeug.Klappen;
import com.teama.skyteam.drop.gui.SchalterGUI;
import com.teama.skyteam.drop.gui.SzenarioHoeheManager;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.KaffeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpielLogik {
    private final Spiel spiel;
    private final Skin skin;
    private final Stage stage;
    private Window window;
    private SchalterGUI schalterGUI;
    private Image ruder;
    private int neuerWert;
    private SzenarioHoeheManager szenarioHoeheManager;

    public SpielLogik(Spiel spiel, Stage stage, Skin skin, SchalterGUI schalterGUI, Image ruder, SzenarioHoeheManager szenarioHoeheManager) {
        this.spiel = spiel;
        this.stage = stage;
        this.skin = skin;
        this.schalterGUI = schalterGUI;
        this.ruder = ruder;
        this.szenarioHoeheManager = szenarioHoeheManager;
    }

    boolean checkAusfallBedingung() {
        boolean bedingungKollision = spiel.isKollision();
        boolean bedingungWenigerAlsSzenarioLange = spiel.getCurrentPosition() > spiel.getRouteSzenario().size();
        boolean bedingungHoehe = !spiel.getCurrentField().istFlughafen() && (spiel.getCurrentHoehe().getHoehe() == 0);
        boolean bedingungNeigung = (spiel.getFlugzeug().getNeigung() > 2 || spiel.getFlugzeug().getNeigung() < -2);
        return bedingungKollision || bedingungWenigerAlsSzenarioLange || bedingungHoehe || bedingungNeigung;
    }

    boolean checkFinalRound() {
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
        return bedingungIstFrei && bedingungNeigung && bedingungKlappen && bedingungFahrgestelle && bedingungSpeed;
    }


    public boolean pilotSchritt(Spieler spieler, String auswahl, Wuerfel gewaehlterWuerfel, int indexWuerfel) {
        Pilot pilot = (Pilot) spieler;
        switch (auswahl) {
            case "PRadio":
                if (!spiel.getFeldPilot("radioFeld").isBelegt()) {
                    radio(gewaehlterWuerfel);
                    pilot.radioWaehlen(gewaehlterWuerfel, indexWuerfel);
                }
                return true;
            case "PSpeed":
                pilot.speedWaehlen(gewaehlterWuerfel, indexWuerfel);
                return true;
            case "PNeigung":
                pilot.neigenWaehlen(gewaehlterWuerfel, indexWuerfel);
                return true;
            case "PBremsen2":
                if (spiel.getFlugzeug().bremsen(gewaehlterWuerfel)) {
                    pilot.bremsenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateBremsenSchalter(0);
                    schalterGUI.moveBremsenIndicator(spiel.getFlugzeug().getBremsenZustand().getValue());
                    return true;
                } else {
                    createMessage("Fehler", "Fehler beim Bremsen! Würfel nicht passend.");
                    return false;
                }
            case "PBremsen4":
                if (gewaehlterWuerfel.getWert() == 4 && spiel.getFlugzeug().bremsen(gewaehlterWuerfel)) {
                    pilot.bremsenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateBremsenSchalter(1);
                    schalterGUI.moveBremsenIndicator(spiel.getFlugzeug().getBremsenZustand().getValue());
                    return true;
                } else {
                    createMessage("Fehler", "Bremsen nicht möglich!");
                    return false;
                }
            case "PBremsen6":
                if (gewaehlterWuerfel.getWert() == 6 && spiel.getFlugzeug().bremsen(gewaehlterWuerfel)) {
                    pilot.bremsenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateBremsenSchalter(2);
                    schalterGUI.moveBremsenIndicator(spiel.getFlugzeug().getBremsenZustand().getValue());
                    return true;
                } else {
                    createMessage("Fehler", "Bremsen nicht möglich!");
                    return false;
                }
            case "PFahrgestell12":
                if ((gewaehlterWuerfel.getWert() == 1 || gewaehlterWuerfel.getWert() == 2) && spiel.getFlugzeug().fahrgestellAusfahren(gewaehlterWuerfel)) {
                    pilot.fahrgestellWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateFahrgestellSchalter(0);
                    schalterGUI.moveFahrgestellIndicator(spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle());
                    return true;
                } else {
                    createMessage("Fehler", "Fahrgestell nicht möglich!");
                    return false;
                }
            case "PFahrgestell34":
                if ((gewaehlterWuerfel.getWert() == 3 || gewaehlterWuerfel.getWert() == 4) && spiel.getFlugzeug().fahrgestellAusfahren(gewaehlterWuerfel)) {
                    pilot.fahrgestellWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateFahrgestellSchalter(1);
                    schalterGUI.moveFahrgestellIndicator(spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle());
                    return true;
                } else {
                    createMessage("Fehler", "Fahrgestell nicht möglich!");
                    return false;
                }
            case "PFahrgestell56":
                if ((gewaehlterWuerfel.getWert() == 5 || gewaehlterWuerfel.getWert() == 6) && spiel.getFlugzeug().fahrgestellAusfahren(gewaehlterWuerfel)) {
                    pilot.fahrgestellWaehlen(gewaehlterWuerfel, indexWuerfel);
                    schalterGUI.activateFahrgestellSchalter(2);
                    schalterGUI.moveFahrgestellIndicator(spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle());
                    return true;
                } else {
                    createMessage("Fehler", "Fahrgestell nicht möglich!");
                    return false;
                }
            case "PilotKaffee1":
                pilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee1");
                return true;
            case "PilotKaffee2":
                pilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee2");
                return true;
            case "PilotKaffee3":
                pilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee3");
                return true;

            default:
                createMessage("Fehler", "Falsche Auswahl für den Piloten!");
                return false;
        }
    }

    public boolean copilotSchritt(Spieler spieler, String auswahl, Wuerfel gewaehlterWuerfel, int indexWuerfel) {
        Copilot copilot = (Copilot) spieler;
        switch (auswahl) {
            case "CRadio1":
                if (!spiel.getFeldCopilot("radioFeld1").isBelegt()) {
                    radio(gewaehlterWuerfel);
                    copilot.radioWaehlen(gewaehlterWuerfel, indexWuerfel);
                }
                return true;
            case "CRadio2":
                if (!spiel.getFeldCopilot("radioFeld2").isBelegt()) {
                    radio(gewaehlterWuerfel);
                    copilot.radioWaehlen(gewaehlterWuerfel, indexWuerfel);
                }
                return true;
            case "CSpeed":
                copilot.speedWaehlen(gewaehlterWuerfel, indexWuerfel);
                return true;
            case "CNeigung":
                copilot.neigenWaehlen(gewaehlterWuerfel, indexWuerfel);
                return true;
            case "CKlappen12":
            case "CKlappen23":
            case "CKlappen45":
            case "CKlappen56":
                if (spiel.getFlugzeug().klappenOeffnen(gewaehlterWuerfel)) {
                    copilot.klappenWaehlen(gewaehlterWuerfel, indexWuerfel);
                    if (gewaehlterWuerfel.getWert() <= 2) {
                        schalterGUI.activateKlappenSchalter(0);
                    } else if (gewaehlterWuerfel.getWert() <= 3) {
                        schalterGUI.activateKlappenSchalter(1);
                    } else if (gewaehlterWuerfel.getWert() <= 5) {
                        schalterGUI.activateKlappenSchalter(2);
                    } else {
                        schalterGUI.activateKlappenSchalter(3);
                    }
                    schalterGUI.moveKlappenIndicator(spiel.getFlugzeug().getKlappenZustand().getValue());
                    return true;
                } else {
                    createMessage("Fehler", "Klappen nicht möglich!");
                    return false;
                }
            case "CopilotKaffee1":
                copilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee1");
                return true;
            case "CopilotKaffee2":
                copilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee2");
                return true;
            case "CopilotKaffee3":
                copilot.kaffeeWaehlen(gewaehlterWuerfel, indexWuerfel, "Kaffee3");
                return true;
            default:
                createMessage("Fehler", "Falsche Auswahl für den Copiloten!");
                return false;
        }
    }

    boolean reiseFeldZustandAktualisieren() {
        boolean speedFeldPilotBelegt = spiel.getFeldPilot("speedFeld").isBelegt();
        boolean speedFeldCopilotBelegt = spiel.getFeldCopilot("speedFeld").isBelegt();
        if (speedFeldPilotBelegt && speedFeldCopilotBelegt) {
            int pilotSpeed = spiel.getFeldPilot("speedFeld").getWuerfel().getWert();
            int copilotSpeed = spiel.getFeldCopilot("speedFeld").getWuerfel().getWert();
            int totalSpeed = pilotSpeed + copilotSpeed;
            spiel.getFlugzeug().setSpeedAktuell(totalSpeed);
            int speedUntereGrenze = spiel.getFlugzeug().getSpeedUntereGrenze().getValue();
            int speedObereGrenze = spiel.getFlugzeug().getSpeedObereGrenze().getValue();
            int steps;
            if (totalSpeed < speedUntereGrenze) {
                steps = 0;
            } else if (totalSpeed <= speedObereGrenze) {
                steps = 1;
            } else {
                steps = 2;
            }
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
            createMessage("Fehler", "Die Zielposition liegt außerhalb des Szenarios.");
        } else {
            ReiseFeld zielFeld = routeSzenario.get(targetField);
            if (zielFeld.getAnzahlFlugzeug() > 0) {
                zielFeld.removeFlugzeug();
            }
        }
    }

    boolean neigenSetzen() {
        float angle = 25f;
        boolean neigenFeldPilotBelegt = spiel.getFeldPilot("neigenFeld").isBelegt();
        boolean neigenFeldCopilotBelegt = spiel.getFeldCopilot("neigenFeld").isBelegt();
        if (neigenFeldPilotBelegt && neigenFeldCopilotBelegt) {
            Wuerfel w1 = spiel.getFeldPilot("neigenFeld").getWuerfel();
            Wuerfel w2 = spiel.getFeldCopilot("neigenFeld").getWuerfel();
            spiel.getFlugzeug().neigen(spiel.getFlugzeug().getNeigung(), new Wuerfel[]{w1, w2});
            float targetRotation;
            if (spiel.getFlugzeug().getNeigung() == 0) {
                targetRotation = 0;
            } else {
                targetRotation = angle * spiel.getFlugzeug().getNeigung();
            }
            ruder.addAction(Actions.rotateTo(targetRotation, 1f));
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
        } else if (steps > 2) {
            createMessage("Fehler", "Der Schritt ist zu groß!");
        } else {
            for (int i = position; i <= newPosition; i++) {
                ReiseFeld currentField = routeSzenario.get(i);
                if (currentField.getAnzahlFlugzeug() > 0) {
                    spiel.setKollision(true);
                }
            }
            spiel.setPosition(newPosition);
            spiel.updateCurrentField();
            szenarioHoeheManager.updateScenario(steps);
        }
    }

    void addNeuWurfTokens() {
        Flughoehe currentHoehe = spiel.getCurrentHoehe();
        if (currentHoehe.hatErneuWerfen()) {
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

    private ArrayList<Wuerfel> werfen() {
        ArrayList<Wuerfel> wurfe = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wurfe.add(new Wuerfel());
        }
        return wurfe;
    }

    void clearManyUseFelds() {
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

    ArrayList<Feld> getOneUseFelds() {
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

    void clearKaffeFeld() {
        int kaffeeCount = spiel.getKaffeTokenCount();
        switch (kaffeeCount) {
            case 0:
                spiel.getFeldPilot("Kaffee1").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee1").setWuerfel(null);
                spiel.getFeldPilot("Kaffee2").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee2").setWuerfel(null);
                spiel.getFeldPilot("Kaffee3").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee3").setWuerfel(null);
                break;
            case 1:
                spiel.getFeldPilot("Kaffee2").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee2").setWuerfel(null);
                spiel.getFeldPilot("Kaffee3").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee3").setWuerfel(null);
                break;
            case 2:
                spiel.getFeldPilot("Kaffee3").setWuerfel(null);
                spiel.getFeldCopilot("Kaffee3").setWuerfel(null);
                break;
        }

    }

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
                        spieler.kaffeeTokenBenutzen(kaffeToken(), index, KaffeTokenAktion.PLUS);
                        neuerWert = spieler.getWuerfels().get(index).getWert();
                        clearKaffeFeld();
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
                        spieler.kaffeeTokenBenutzen(kaffeToken(), index, KaffeTokenAktion.MINUS);
                        neuerWert = spieler.getWuerfels().get(index).getWert();
                        clearKaffeFeld();
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
            path = "images/spiel/main/dog-happy.png";
            w = 800;
            h = 550;
            buttonText = "Hurra!";
            title = "Sieg!";
        } else {
            path = "images/spiel/main/frog-sad.png";
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
