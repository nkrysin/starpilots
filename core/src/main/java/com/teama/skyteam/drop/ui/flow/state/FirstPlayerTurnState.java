package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.ui.flow.GameBoard;
import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

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

            // Pre-set next state — triggered when player places a die via callback
            context.setState(afterTurn);
            // Do NOT call executeCurrentState() — wait for player input
        } else {
            // No dice — skip turn, go directly to next
            context.setState(afterTurn);
            context.executeCurrentState();
        }
    }
}