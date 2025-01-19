/**
 * Konstruktor-Klasse zur Erstellung eines einzelnen Spielobjekts (Singleton).
 *
 * Die Klasse `SpielConstructor` verfolgt das Singleton-Designmuster, um sicherzustellen, dass zu jedem
 * Zeitpunkt nur ein einziges Spielobjekt existiert. Dies ermöglicht eine zentrale Verwaltung des Spiels
 * und seiner Komponenten.
 */
package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.flugzeug.Flugzeug;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.szenario.Route;


public class SpielConstructor {
    private static Spiel spiel; // Statische Variable zur Speicherung der einzigen Instanz des Spiels.

    // Privater Konstruktor, um die direkte Instanziierung dieser Klasse zu verhindern.
    private SpielConstructor() {
    }

    /**
     * Erstellt ein neues Spielobjekt, falls noch keines existiert, oder setzt ein bestehendes zurück.
     *
     * @param route Die Route, die als Szenario für das neue Spiel verwendet wird.
     * @return Die einzige Instanz des Spiels.
     */
    public static Spiel createSpiel(Route route) {
        // Setzt das Spiel zurück, falls bereits eine Instanz existiert.
        if (spiel != null) {
            spiel = null;
        }
        // Erstellt eine neue Instanz des Spiels und initialisiert alle Komponenten.
        if (spiel == null) {
            spiel = new Spiel();
            spiel.setFlugzeug(new Flugzeug()); // Initialisierung des Flugzeugs.

            // Erstellen und Zuweisen des Piloten.
            Pilot pilot = new Pilot(spiel);
            spiel.setPilot(pilot);

            // Erstellen und Zuweisen des Copiloten.
            Copilot copilot = new Copilot(spiel);
            spiel.setCopilot(copilot);

            // Initialisierung der Spielfelder für Pilot und Copilot.
            spiel.setPilotFelds();
            spiel.setCopilotFelds();

            // Festlegen des Szenarios basierend auf der gegebenen Route.
            spiel.setSzenario(route);

            // Aktualisierung des aktuellen Feldes und der Flughöhe.
            spiel.updateCurrentField();
            spiel.updateCurrentHoehe();

            // Initialisierung des Routen- und Höhen-Szenarios.
            spiel.setRouteSzenario();
            spiel.setHoeheSzenario();
        }

        // Rückgabe der einzigen Spielinstanz.
        return spiel;
    }
}
