/**
 * Konstruktor-Klasse zur Erstellung eines einzelnen Spielobjekts (Singleton).
 *
 * Die Klasse `GameConstructor` verfolgt das Singleton-Designmuster, um sicherzustellen, dass zu jedem
 * Zeitpunkt nur ein einziges Spielobjekt existiert. Dies ermöglicht eine zentrale Verwaltung des Spiels
 * und seiner Komponenten.
 */
package com.teama.skyteam.drop.domain.model.game;

import com.teama.skyteam.drop.domain.model.plane.Plane;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.campaign.Route;


public class GameConstructor {
    private static Game instance; // Statische Variable zur Speicherung der einzigen Instanz des Spiels.

    // Privater Konstruktor, um die direkte Instanziierung dieser Klasse zu verhindern.
    private GameConstructor() {
    }

    /**
     * Erstellt ein neues Spielobjekt, falls noch keines existiert, oder setzt ein bestehendes zurück.
     *
     * @param route Die Route, die als Campaign für das neue Game verwendet wird.
     * @return Die einzige Instanz des Spiels.
     */
    public static Game getInstance(Route route) {
        // Setzt das Game zurück, falls bereits eine Instanz existiert.
        if (instance != null) {
            instance = null;
        }
        // Erstellt eine neue Instanz des Spiels und initialisiert alle Komponenten.
        if (instance == null) {
            instance = new Game();
            instance.setPlane(new Plane()); // Initialisierung des Flugzeugs.

            // Erstellen und Zuweisen des Piloten.
            Pilot pilot = new Pilot(instance);
            instance.setPilot(pilot);

            // Erstellen und Zuweisen des Copiloten.
            Copilot copilot = new Copilot(instance);
            instance.setCopilot(copilot);

            // Initialisierung der Spielfelder für Pilot und Copilot.
            instance.setPilotFields();
            instance.setCopilotFields();

            // Festlegen des Szenarios basierend auf der gegebenen Route.
            instance.setCampaign(route);

            // Aktualisierung des aktuellen Feldes und der Flughöhe.
            instance.updateCurrentField();
            instance.updateCurrentAltitude();

            // Initialisierung des Routen- und Höhen-Szenarios.
            instance.setRouteCampaign();
            instance.setAltitudeCampaign();
        }

        // Rückgabe der einzigen Spielinstanz.
        return instance;
    }
}
