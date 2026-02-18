package com.teama.skyteam.drop.ui.flow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teama.skyteam.drop.application.gameservice.GameService;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.gamestate.StandardGameStateManager;
import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.application.logic.fields.StandardFieldManager;
import com.teama.skyteam.drop.domain.action.factory.ActionFactory;
import com.teama.skyteam.drop.domain.action.validator.ActionValidator;
import com.teama.skyteam.drop.domain.action.validator.StandardActionValidator;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.domain.rules.GameRules;
import com.teama.skyteam.drop.domain.rules.StandartGameRules;
import com.teama.skyteam.drop.ui.flow.state.PreRoundEvaluationState;
import com.teama.skyteam.drop.ui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.ui.view.CampaignAltitudeView;

public class GameFlowController implements GameFlowContext {

    private final Game game;
    private final GameService gameService;
    private final CampaignAltitudeView campaignAltitudeView;
    private final GameBoard gameBoard;
    private final GameStateManager gameStateManager;
    private final FieldManager fieldManager;

    private GameFlowState currentState;
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean lastRound = false;
    private boolean tiltCompleted = false;
    private boolean speedCompleted = false;
    private boolean endgame = false;

    public GameFlowController(Game game, CampaignAltitudeView campaignAltitudeView, Stage stage, Skin skin) {
        this.game = game;
        this.campaignAltitudeView = campaignAltitudeView;
        this.fieldManager = new StandardFieldManager();
        this.gameStateManager = new StandardGameStateManager(game);

        ActionValidator validator = new StandardActionValidator();
        ActionFactory actionFactory = new ActionFactory(game, gameStateManager);
        GameRules gameRules = new StandartGameRules();
        this.gameService = new GameService(game, gameRules, validator, actionFactory, gameStateManager);

        this.gameBoard = new GameBoard(game, stage, skin, campaignAltitudeView, gameService, gameStateManager, fieldManager);

        this.gameBoard.setPlayerActionCallback(new PlayerActionCallback() {
            @Override
            public void onPlayerActionCompleted(Player player) {
                gameBoard.clearPlayerListeners(player);
                executeCurrentState();
            }
        });

        this.currentState = new PreRoundEvaluationState();
        executeCurrentState();
    }

    // State Machine
    @Override
    public void setState(GameFlowState nextState) {
        this.currentState = nextState;
    }

    @Override
    public void executeCurrentState() {
        if (endgame) return;
        currentState.execute(this);
    }

    @Override
    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

    // Core Services
    @Override
    public GameService getGameService() {
        return gameService;
    }

    @Override
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    @Override
    public FieldManager getFieldManager() {
        return fieldManager;
    }

    @Override
    public CampaignAltitudeView getCampaignAltitudeView() {
        return campaignAltitudeView;
    }

    // Players

    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    @Override
    public Player getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public void setFirstPlayer(Player player) {
        this.firstPlayer = player;
    }

    @Override
    public void setSecondPlayer(Player player) {
        this.secondPlayer = player;
    }

    // Round flags
    @Override
    public boolean isLastRound() {
        return lastRound;
    }

    @Override
    public void markLastRound() {
        this.lastRound = true;
    }

    @Override
    public boolean isTiltCompleted() {
        return tiltCompleted;
    }

    @Override
    public void markTiltCompleted() {
        this.tiltCompleted = true;
    }

    @Override
    public boolean isSpeedCompleted() {
        return speedCompleted;
    }

    @Override
    public void markSpeedCompleted() {
        this.speedCompleted = true;
    }

    @Override
    public void resetRoundFlags() {
        this.tiltCompleted = false;
        this.speedCompleted = false;
    }


    // Render
    public void render() {
        gameBoard.stage.act(Gdx.graphics.getDeltaTime());
        gameBoard.stage.draw();
    }

    public void resize(int width, int height) {
        gameBoard.stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        if (gameBoard != null) {
            gameBoard.dispose();
        }
    }
}
