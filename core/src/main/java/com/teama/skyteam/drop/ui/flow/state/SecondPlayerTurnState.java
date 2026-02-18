package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.ui.flow.GameBoard;
import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

public class SecondPlayerTurnState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        Player secondPlayer = context.getSecondPlayer();
        GameBoard gameBoard = context.getGameBoard();

        GameFlowState afterTurn = new ResolveTiltState(new ResolveSpeedState(new RoundCleanupState()));

        if (secondPlayer.getAllDiceCount() > 0 && !context.getGameService().checkFailCondition()) {
            gameBoard.getDiceInputHandler().removeAllHideImages();
            gameBoard.getDiceInputHandler().hideDice(secondPlayer);
            gameBoard.getDiceInputHandler().addDiceListenersForSpieler(secondPlayer);
            gameBoard.addFieldListenersForPlacement(secondPlayer);
            gameBoard.checkFieldSituation(secondPlayer);
            gameBoard.setCoffeeGroupsActive(secondPlayer);

            if (secondPlayer instanceof Pilot) {
                gameBoard.showCoffee(true, false);
            } else {
                gameBoard.showCoffee(false, true);
            }
            gameBoard.showReroll(true);
            gameBoard.useReroll(secondPlayer);
            gameBoard.useCoffee(secondPlayer);

            context.setState(afterTurn);
        } else {
            context.setState(afterTurn);
            context.executeCurrentState();
        }
    }
}
