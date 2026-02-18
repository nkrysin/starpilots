package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

public class ResolveTiltState implements GameFlowState {

    private final GameFlowState nextState;

    public ResolveTiltState(GameFlowState nextState) {
        this.nextState = nextState;
    }

    @Override
    public void execute(GameFlowContext context) {
        if (!context.isTiltCompleted() && context.getGameService().checkTilt().isSuccess()) {
            context.markTiltCompleted();
        }
        context.setState(nextState);
        context.executeCurrentState();
    }
}