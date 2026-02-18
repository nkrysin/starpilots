package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

public class ResolveSpeedState implements GameFlowState {

    private final GameFlowState nextState;

    public ResolveSpeedState(GameFlowState nextState) {
        this.nextState = nextState;
    }

    @Override
    public void execute(GameFlowContext context) {
        if (!context.isSpeedCompleted() && context.getGameService().checkSpeed().isSuccess()) {
            context.markSpeedCompleted();
        }
        context.setState(nextState);
        context.executeCurrentState();
    }
}