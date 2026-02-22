package com.teama.starpilots.drop.ui.flow.state;

import com.teama.starpilots.drop.domain.model.player.Pilot;
import com.teama.starpilots.drop.domain.model.player.Player;
import com.teama.starpilots.drop.ui.flow.GameBoard;
import com.teama.starpilots.drop.ui.flow.GameFlowContext;
import com.teama.starpilots.drop.ui.flow.GameFlowState;

public class FirstPlayerTurnState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        Player firstPlayer = context.getFirstPlayer();
        GameBoard gameBoard = context.getGameBoard();

        GameFlowState afterTurn = new ResolveTiltState(new ResolveSpeedState(new SecondPlayerTurnState()));

        if (firstPlayer.getAllDiceCount() > 0 && !context.getGameService().checkFailCondition()) {
            gameBoard.getDiceInputHandler().removeAllHideImages();
            gameBoard.getDiceInputHandler().hideDice(firstPlayer);
            gameBoard.getDiceInputHandler().addDiceListenersForSpieler(firstPlayer);
            gameBoard.addFieldListenersForPlacement(firstPlayer);
            gameBoard.checkFieldSituation(firstPlayer);
            gameBoard.setCoffeeGroupsActive(firstPlayer);

            if (firstPlayer instanceof Pilot) {
                gameBoard.showCoffee(true, false);
            } else {
                gameBoard.showCoffee(false, true);
            }
            gameBoard.showReroll(true);
            gameBoard.useCoffee(firstPlayer);
            gameBoard.useReroll(firstPlayer);

            context.setState(afterTurn);
        } else {
            context.setState(afterTurn);
            context.executeCurrentState();
        }
    }
}
