package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

public class PreRoundEvaluationState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        if (context.getGameService().checkFailCondition()) {
            context.setState(new GameOverState());
            context.executeCurrentState();
        } else {
            if (!context.isLastRound() && context.getGameService().checkFinalRound()) {
                context.markLastRound();
            }
            context.setState(new RoundSetupState());
            context.executeCurrentState();
        }
    }
}