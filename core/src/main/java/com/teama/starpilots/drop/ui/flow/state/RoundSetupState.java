package com.teama.starpilots.drop.ui.flow.state;

import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.model.player.Player;
import com.teama.starpilots.drop.ui.flow.GameFlowContext;
import com.teama.starpilots.drop.ui.flow.GameFlowState;

public class RoundSetupState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        context.resetRoundFlags();

        Game game = context.getGame();
        context.getGameStateManager().addRerollTokens();

        boolean isEvenRound = (game.getRound() + 1) % 2 == 0;
        Player firstPlayer = isEvenRound ? game.getPilot() : game.getCopilot();
        Player secondPlayer = isEvenRound ? game.getCopilot() : game.getPilot();

        context.setFirstPlayer(firstPlayer);
        context.setSecondPlayer(secondPlayer);

        context.getGameStateManager().setNewDices(firstPlayer);
        context.getGameStateManager().setNewDices(secondPlayer);

        context.getGameBoard().getDiceInputHandler().createDiceForPlayer(firstPlayer);
        context.getGameBoard().getDiceInputHandler().createDiceForPlayer(secondPlayer);

        context.setState(new FirstPlayerTurnState());
        context.executeCurrentState();
    }
}
