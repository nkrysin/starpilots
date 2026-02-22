/**
 * Schnittstelle zur Verarbeitung von Spieleraktionen.
 *
 * Die Schnittstelle `PlayerActionCallback` dient als Callback-Mechanismus,
 * um Benachrichtigungen zu erhalten, wenn ein Player eine Aktion abgeschlossen hat.
 * Sie ermöglicht eine lose Kopplung zwischen Komponenten, die Spieleraktionen ausführen,
 * und denjenigen, die auf den Abschluss dieser Aktionen reagieren müssen.
 */
package com.teama.starpilots.drop.ui.utilities;
import com.teama.starpilots.drop.domain.model.player.Player;

public interface PlayerActionCallback {
    /**
     * Wird aufgerufen, wenn ein Player eine Aktion abgeschlossen hat.
     *
     * @param player der Player, der die Aktion abgeschlossen hat.
     */
    void onPlayerActionCompleted(Player player);
}
