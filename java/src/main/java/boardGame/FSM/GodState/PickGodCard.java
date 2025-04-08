package boardGame.FSM.GodState;

import boardGame.GameState;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.IState;
import boardGame.FSM.InitializationState;
import boardGame.GameMode.GodCardMode;

public class PickGodCard implements IState {
    public GodCardMode godMode;

    @Override
    public void onEnter(GameState state) {

    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void specialAction(String content, GameState state) {
        if(godMode == null) return;

        godMode.updateGodCards(state.currentPlayerID(), content, state);

        state.changeToNextPlayer();
        if(state.currentPlayer == state.players.get(0)){
            state.changeState(new InitializationState());
        }
    }

    @Override 
    public String toString() {
        return "pickGodCard";
    }
    
}
