/**
 * Verwaltert die Eigenschaften und Zustände eines Flugzeugs im Game.
 *
 * Die Klasse `Plane` enthält grundlegende Attribute wie die aktuelle Geschwindigkeit, die Neigung,
 * den Zustand der Brakes, der Flaps und des Fahrgestells. Sie stellt Methoden zur Verwaltung und
 * Aktualisierung dieser Eigenschaften zur Verfügung, die im Game verwendet werden, um das Plane
 * zu steuern und zu verändern.
 */
package com.teama.skyteam.drop.domain.model.flugzeug;

import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class Plane {
    private int currentSpeed;
    private Speed speedLowerLimit;
    private Speed speedUpperLimit;
    private int tilt;
    private Brakes brakesState;
    private Flaps flapsZustand;
    private LandingGear landingGearZustand;


    /**
     * Konstruktor für die Klasse Plane.
     *
     * Initialisiert ein neues Plane-Objekt entsprechend den Spielregeln:
     * - Die aktuelle Geschwindigkeit wird auf 0 gesetzt.
     * - Die untere Geschwindigkeitsgrenze wird auf Speed.FOUR gesetzt.
     * - Die obere Geschwindigkeitsgrenze wird auf Speed.EIGHT gesetzt.
     * - Die Neigung wird auf 0 gesetzt.
     * - Der Bremsenzustand wird auf Brakes.NONE gesetzt.
     * - Der Klappenzustand wird auf Flaps.NONE gesetzt.
     * - Der Fahrgestellzustand wird auf ein neues LandingGear-Objekt gesetzt.
     */
    public Plane() {
        setCurrentSpeed(0);
        setSpeedLowerLimit(Speed.FOUR);
        setSpeedUpperLimit(Speed.EIGHT);
        setTilt(0);
        setBremsenZustand(Brakes.NONE);
        setKlappenZustand(Flaps.NONE);
        setFahrgestellZustand(new LandingGear());
    }

    /*Getters and Setters*/
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Speed getSpeedLowerLimit() {
        return speedLowerLimit;
    }

    void setSpeedLowerLimit(Speed speedLowerLimit) {
        this.speedLowerLimit = speedLowerLimit;
    }

    public Speed getSpeedUpperLimit() {
        return speedUpperLimit;
    }

    void setSpeedUpperLimit(Speed speedUpperLimit) {
        this.speedUpperLimit = speedUpperLimit;
    }

    public int getTilt() {
        return tilt;
    }

    void setTilt(int tilt) {
        this.tilt = tilt;
    }


    public Brakes getBrakesState() {
        return brakesState;
    }

    void setBremsenZustand(Brakes brakesState) {
        this.brakesState = brakesState;
    }

    public Flaps getKlappenZustand() {
        return flapsZustand;
    }

    void setKlappenZustand(Flaps flapsZustand) {
        this.flapsZustand = flapsZustand;
    }

    public LandingGear getFahrgestellZustand() {
        return landingGearZustand;
    }

    void setFahrgestellZustand(LandingGear landingGearZustand) {
        this.landingGearZustand = landingGearZustand;
    }


    /**
     * Subtrahiert die Werte zweier Würfel.
     *
     * @param dice ein Array von zwei Würfeln, deren Werte subtrahiert werden.
     * @return die Differenz zwischen den Werten der beiden Würfel.
     * @throws IllegalArgumentException wenn das Array null ist oder nicht genau zwei Würfel enthält.
     */
    private int zweiWuerfelWertSubtrahieren(Dice[] dice) {
        if (dice == null || dice.length != 2) {
            throw new IllegalArgumentException("Falsche Anzahl von Würfeln wurden gegeben");
        }
        int sub = dice[0].getWert() - dice[1].getWert();
        return sub;
    }

    /**
     * Ändert die Neigung des Flugzeugs basierend auf einem gegebenen Wert und den Würfelwerten.
     *
     * @param neigung die Basisneigung, die geändert werden soll.
     * @param dice ein Array von zwei Würfeln, deren Wertunterschied zur Neigung addiert wird.
     */
    public void neigen(int neigung, Dice[] dice) {
        int sub = zweiWuerfelWertSubtrahieren(dice);
        setTilt(neigung + sub);
    }

    /**
     * Aktiviert die Brakes basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert die nächste Bremsstufe bestimmt.
     * @return true, wenn der Bremszustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean bremsen(Dice dice) {
        int wert = dice.getWert();
        Brakes aktuellerZustand = getBrakesState();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 2) {
                    setBremsenZustand(Brakes.LEVEL1);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 4) {
                    setBremsenZustand(Brakes.LEVEL2);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 6) {
                    setBremsenZustand(Brakes.LEVEL3);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Öffnet die Flaps basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert die nächste Klappenstufe bestimmt.
     * @return true, wenn der Klappenzustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean klappenOeffnen(Dice dice) {
        int wert = dice.getWert();
        Flaps aktuellerZustand = getKlappenZustand();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 1 || wert == 2) {
                    setKlappenZustand(Flaps.LEVEL1);
                    setSpeedUpperLimit(Speed.NINE);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 2 || wert == 3) {
                    setKlappenZustand(Flaps.LEVEL2);
                    setSpeedUpperLimit(Speed.TEN);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 4 || wert == 5) {
                    setKlappenZustand(Flaps.LEVEL3);
                    setSpeedUpperLimit(Speed.ELEVEN);
                    return true;
                }
                break;
            case LEVEL3:
                if (wert == 5 || wert == 6) {
                    setKlappenZustand(Flaps.LEVEL4);
                    setSpeedUpperLimit(Speed.TWELVE);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Aktiviert ein bestimmtes LandingGear basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert das zu aktivierende LandingGear bestimmt.
     * @return true, wenn das LandingGear erfolgreich aktiviert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean fahrgestellAusfahren(Dice dice) {
        int wert = dice.getWert();
        switch (wert) {
            case 1, 2:
                getFahrgestellZustand().fahrgestellAktivieren(1);
                speedUntereGrenzeAktualisieren();
                return true;
            case 3, 4:
                getFahrgestellZustand().fahrgestellAktivieren(2);
                speedUntereGrenzeAktualisieren();
                return true;
            case 5, 6:
                getFahrgestellZustand().fahrgestellAktivieren(3);
                speedUntereGrenzeAktualisieren();
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Aktualisiert die untere Geschwindigkeitsgrenze basierend auf der Anzahl der aktivierten Fahrgestelle.
     */
    private void speedUntereGrenzeAktualisieren() {
        int akrivierteFahrgestelle = getFahrgestellZustand().getAkrivierteFahrgestelle();
        switch (akrivierteFahrgestelle) {
            case 1 -> setSpeedLowerLimit(Speed.FIVE);
            case 2 -> setSpeedLowerLimit(Speed.SIX);
            case 3 -> setSpeedLowerLimit(Speed.SEVEN);
        }
    }
}






