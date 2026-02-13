/**
 * Verwaltert die Eigenschaften und Zustände eines Flugzeugs im Spiel.
 *
 * Die Klasse `Flugzeug` enthält grundlegende Attribute wie die aktuelle Geschwindigkeit, die Neigung,
 * den Zustand der Bremsen, der Klappen und des Fahrgestells. Sie stellt Methoden zur Verwaltung und
 * Aktualisierung dieser Eigenschaften zur Verfügung, die im Spiel verwendet werden, um das Flugzeug
 * zu steuern und zu verändern.
 */
package com.teama.skyteam.drop.domain.model.flugzeug;

import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class Flugzeug {
    private int speedAktuell;
    private Speed speedUntereGrenze;
    private Speed speedObereGrenze;
    private int neigung;
    private Bremsen bremsenZustand;
    private Klappen klappenZustand;
    private Fahrgestell fahrgestellZustand;


    /**
     * Konstruktor für die Klasse Flugzeug.
     *
     * Initialisiert ein neues Flugzeug-Objekt entsprechend den Spielregeln:
     * - Die aktuelle Geschwindigkeit wird auf 0 gesetzt.
     * - Die untere Geschwindigkeitsgrenze wird auf Speed.FOUR gesetzt.
     * - Die obere Geschwindigkeitsgrenze wird auf Speed.EIGHT gesetzt.
     * - Die Neigung wird auf 0 gesetzt.
     * - Der Bremsenzustand wird auf Bremsen.NONE gesetzt.
     * - Der Klappenzustand wird auf Klappen.NONE gesetzt.
     * - Der Fahrgestellzustand wird auf ein neues Fahrgestell-Objekt gesetzt.
     */
    public Flugzeug() {
        setSpeedAktuell(0);
        setSpeedUntereGrenze(Speed.FOUR);
        setSpeedObereGrenze(Speed.EIGHT);
        setNeigung(0);
        setBremsenZustand(Bremsen.NONE);
        setKlappenZustand(Klappen.NONE);
        setFahrgestellZustand(new Fahrgestell());
    }

    /*Getters and Setters*/
    public int getSpeedAktuell() {
        return speedAktuell;
    }

    public void setSpeedAktuell(int speedAktuell) {
        this.speedAktuell = speedAktuell;
    }

    public Speed getSpeedUntereGrenze() {
        return speedUntereGrenze;
    }

    void setSpeedUntereGrenze(Speed speedUntereGrenze) {
        this.speedUntereGrenze = speedUntereGrenze;
    }

    public Speed getSpeedObereGrenze() {
        return speedObereGrenze;
    }

    void setSpeedObereGrenze(Speed speedObereGrenze) {
        this.speedObereGrenze = speedObereGrenze;
    }

    public int getNeigung() {
        return neigung;
    }

    void setNeigung(int neigung) {
        this.neigung = neigung;
    }


    public Bremsen getBremsenZustand() {
        return bremsenZustand;
    }

    void setBremsenZustand(Bremsen bremsenZustand) {
        this.bremsenZustand = bremsenZustand;
    }

    public Klappen getKlappenZustand() {
        return klappenZustand;
    }

    void setKlappenZustand(Klappen klappenZustand) {
        this.klappenZustand = klappenZustand;
    }

    public Fahrgestell getFahrgestellZustand() {
        return fahrgestellZustand;
    }

    void setFahrgestellZustand(Fahrgestell fahrgestellZustand) {
        this.fahrgestellZustand = fahrgestellZustand;
    }


    /**
     * Subtrahiert die Werte zweier Würfel.
     *
     * @param wuerfel ein Array von zwei Würfeln, deren Werte subtrahiert werden.
     * @return die Differenz zwischen den Werten der beiden Würfel.
     * @throws IllegalArgumentException wenn das Array null ist oder nicht genau zwei Würfel enthält.
     */
    private int zweiWuerfelWertSubtrahieren(Wuerfel[] wuerfel) {
        if (wuerfel == null || wuerfel.length != 2) {
            throw new IllegalArgumentException("Falsche Anzahl von Würfeln wurden gegeben");
        }
        int sub = wuerfel[0].getWert() - wuerfel[1].getWert();
        return sub;
    }

    /**
     * Ändert die Neigung des Flugzeugs basierend auf einem gegebenen Wert und den Würfelwerten.
     *
     * @param neigung die Basisneigung, die geändert werden soll.
     * @param wuerfel ein Array von zwei Würfeln, deren Wertunterschied zur Neigung addiert wird.
     */
    public void neigen(int neigung, Wuerfel[] wuerfel) {
        int sub = zweiWuerfelWertSubtrahieren(wuerfel);
        setNeigung(neigung + sub);
    }

    /**
     * Aktiviert die Bremsen basierend auf dem Wert eines Würfels.
     *
     * @param wuerfel ein Würfel, dessen Wert die nächste Bremsstufe bestimmt.
     * @return true, wenn der Bremszustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean bremsen(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
        Bremsen aktuellerZustand = getBremsenZustand();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 2) {
                    setBremsenZustand(Bremsen.LEVEL1);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 4) {
                    setBremsenZustand(Bremsen.LEVEL2);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 6) {
                    setBremsenZustand(Bremsen.LEVEL3);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Öffnet die Klappen basierend auf dem Wert eines Würfels.
     *
     * @param wuerfel ein Würfel, dessen Wert die nächste Klappenstufe bestimmt.
     * @return true, wenn der Klappenzustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean klappenOeffnen(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
        Klappen aktuellerZustand = getKlappenZustand();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 1 || wert == 2) {
                    setKlappenZustand(Klappen.LEVEL1);
                    setSpeedObereGrenze(Speed.NINE);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 2 || wert == 3) {
                    setKlappenZustand(Klappen.LEVEL2);
                    setSpeedObereGrenze(Speed.TEN);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 4 || wert == 5) {
                    setKlappenZustand(Klappen.LEVEL3);
                    setSpeedObereGrenze(Speed.ELEVEN);
                    return true;
                }
                break;
            case LEVEL3:
                if (wert == 5 || wert == 6) {
                    setKlappenZustand(Klappen.LEVEL4);
                    setSpeedObereGrenze(Speed.TWELVE);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Aktiviert ein bestimmtes Fahrgestell basierend auf dem Wert eines Würfels.
     *
     * @param wuerfel ein Würfel, dessen Wert das zu aktivierende Fahrgestell bestimmt.
     * @return true, wenn das Fahrgestell erfolgreich aktiviert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean fahrgestellAusfahren(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
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
            case 1 -> setSpeedUntereGrenze(Speed.FIVE);
            case 2 -> setSpeedUntereGrenze(Speed.SIX);
            case 3 -> setSpeedUntereGrenze(Speed.SEVEN);
        }
    }
}






