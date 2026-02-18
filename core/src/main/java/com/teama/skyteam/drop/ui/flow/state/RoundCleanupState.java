package com.teama.skyteam.drop.ui.flow.state;

import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.ui.flow.GameBoard;
import com.teama.skyteam.drop.ui.flow.GameFlowContext;
import com.teama.skyteam.drop.ui.flow.GameFlowState;

import java.util.ArrayList;

public class RoundCleanupState implements GameFlowState {
    @Override
    public void execute(GameFlowContext context) {
        Game game = context.getGame();
        Player firstPlayer = context.getFirstPlayer();
        Player secondPlayer = context.getSecondPlayer();

        if (!context.getGameService().checkFailCondition() &&
            (firstPlayer.getAllDiceCount() > 0 || secondPlayer.getAllDiceCount() > 0)) {
            context.setState(new FirstPlayerTurnState());
            context.executeCurrentState();
        } else {
            finishRound(context);

            if (context.getGameService().checkFailCondition()) {
                context.setState(new GameOverState());
                context.executeCurrentState();
            } else if (context.isLastRound()) {
                context.setState(new VictoryEvaluationState());
                context.executeCurrentState();
            } else {
                context.setState(new PreRoundEvaluationState());
                context.executeCurrentState();
            }
        }
    }

    private void finishRound(GameFlowContext context) {
        Game game = context.getGame();
        FieldManager fieldManager = context.getFieldManager();
        GameBoard gameBoard = context.getGameBoard();

        ArrayList<Field> oneUseFields = (ArrayList<Field>) fieldManager.getOneUseFields(game);
        fieldManager.removeDiceFromOneUseFields(oneUseFields);
        fieldManager.clearManyUseFields(game);
        gameBoard.clearManyUseFields();
        context.getCampaignAltitudeView().updateAltitude();

        game.setRound(game.getRound() + 1);
        context.getGameStateManager().reduceAltitude();
        game.setEndCondition(context.getGameService().checkFailCondition());
        gameBoard.getDiceInputHandler().removeAllHideImages();
    }
}
