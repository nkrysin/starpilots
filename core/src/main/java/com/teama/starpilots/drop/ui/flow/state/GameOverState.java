package com.teama.starpilots.drop.ui.flow.state;

import com.teama.starpilots.drop.ui.flow.GameFlowContext;
import com.teama.starpilots.drop.ui.flow.GameFlowState;

public class GameOverState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        context.getGameBoard().getDialogManager().createFinalWindow(false);
        context.setEndgame(true);
    }
}
