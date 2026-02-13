/**
 * Konstruktor-Klasse zur Erstellung eines einzelnen Spielobjekts (Singleton).
 *
 * Die Klasse `GameConstructor` verfolgt das Singleton-Designmuster, um sicherzustellen, dass zu jedem
 * Zeitpunkt nur ein einziges Spielobjekt existiert. Dies ermöglicht eine zentrale Verwaltung des Spiels
 * und seiner Komponenten.
 */
package com.teama.skyteam.drop.domain.model.game;

import com.teama.skyteam.drop.domain.model.flugzeug.Plane;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.campaign.Route;


public class GameConstructor {
    private static Game game; // Statische Variable zur Speicherung der einzigen Instanz des Spiels.

    // Privater Konstruktor, um die direkte Instanziierung dieser Klasse zu verhindern.
    private GameConstructor() {
    }

    /**
     * Erstellt ein neues Spielobjekt, falls noch keines existiert, oder setzt ein bestehendes zurück.
     *
     * @param route Die Route, die als Campaign für das neue Game verwendet wird.
     * @return Die einzige Instanz des Spiels.
     */
    public static Game createSpiel(Route route) {
        // Setzt das Game zurück, falls bereits eine Instanz existiert.
        if (game != null) {
            game = null;
        }
        // Erstellt eine neue Instanz des Spiels und initialisiert alle Komponenten.
        if (game == null) {
            game = new Game();
            game.setFlugzeug(new Plane()); // Initialisierung des Flugzeugs.

            // Erstellen und Zuweisen des Piloten.
            Pilot pilot = new Pilot(game);
            game.setPilot(pilot);

            // Erstellen und Zuweisen des Copiloten.
            Copilot copilot = new Copilot(game);
            game.setCopilot(copilot);

            // Initialisierung der Spielfelder für Pilot und Copilot.
            game.setPilotFelds();
            game.setCopilotFelds();

            // Festlegen des Szenarios basierend auf der gegebenen Route.
            game.setSzenario(route);

            // Aktualisierung des aktuellen Feldes und der Flughöhe.
            game.updateCurrentField();
            game.updateCurrentHoehe();

            // Initialisierung des Routen- und Höhen-Szenarios.
            game.setRouteSzenario();
            game.setHoeheSzenario();
        }

        // Rückgabe der einzigen Spielinstanz.
        return game;
    }
}
