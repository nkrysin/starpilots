package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

public class VictoryEvaluationState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        boolean won = context.getGameService().checkWinCondition();
        context.getGameBoard().getDialogManager().createFinalWindow(won);
        context.setEndgame(true);
    }
}