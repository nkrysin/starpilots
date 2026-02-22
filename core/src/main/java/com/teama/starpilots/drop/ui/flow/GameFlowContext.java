package com.teama.starpilots.drop.ui.flow;

import com.teama.starpilots.drop.application.gameservice.GameService;
import com.teama.starpilots.drop.application.gamestate.GameStateManager;
import com.teama.starpilots.drop.application.logic.fields.FieldManager;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.model.player.Player;
import com.teama.starpilots.drop.ui.view.CampaignAltitudeView;

public interface GameFlowContext {

    // State machine
    void setState(GameFlowState nextState);
    void executeCurrentState();
    void setEndgame(boolean endgame);

    // Core services
    GameService getGameService();
    GameBoard getGameBoard();
    Game getGame();
    GameStateManager getGameStateManager();
    FieldManager getFieldManager();
    CampaignAltitudeView getCampaignAltitudeView();

    // Players
    Player getFirstPlayer();
    Player getSecondPlayer();
    void setFirstPlayer(Player player);
    void setSecondPlayer(Player player);

    // Round flags
    boolean isLastRound();
    void markLastRound();
    boolean isTiltCompleted();
    void markTiltCompleted();
    boolean isSpeedCompleted();
    void markSpeedCompleted();
    void resetRoundFlags();
}
