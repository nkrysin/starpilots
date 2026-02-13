/**
 * Schnittstelle zur Verarbeitung von Spieleraktionen.
 *
 * Die Schnittstelle `PlayerActionCallback` dient als Callback-Mechanismus,
 * um Benachrichtigungen zu erhalten, wenn ein Spieler eine Aktion abgeschlossen hat.
 * Sie ermöglicht eine lose Kopplung zwischen Komponenten, die Spieleraktionen ausführen,
 * und denjenigen, die auf den Abschluss dieser Aktionen reagieren müssen.
 */
package com.teama.skyteam.drop.gui.utilities;
import com.teama.skyteam.drop.domain.model.spieler.Spieler;

public interface PlayerActionCallback {
    /**
     * Wird aufgerufen, wenn ein Spieler eine Aktion abgeschlossen hat.
     *
     * @param spieler der Spieler, der die Aktion abgeschlossen hat.
     */
    void onPlayerActionCompleted(Spieler spieler);
}
