package boardGame.FSM;

import boardGame.GameState;
import boardGame.DataStructure.PositionInfo;

public class InitializationState implements IState{

    public GameState state;

    @Override
    public void onEnter(GameState state) {
        this.state = state;
    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        boolean performSuccess = state.initiallySetWorkerPosition(state.currentPlayer, state.currentPlayer.currentWorker(), targetPosition);
        if(!performSuccess) return false;

        state.currentPlayer.changeToNextWorker();
        if(state.currentPlayer.currentWorkerID() == 0){
            state.changeToNextPlayer();
            if(state.currentPlayer == state.players.get(0)){
                state.changeState(state.mode.getNextState(this));
            }
        }

        return performSuccess;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String toString(){
        return "initialization";
    }

    @Override
    public void specialAction(String content, GameState state) {
    }
    
}
