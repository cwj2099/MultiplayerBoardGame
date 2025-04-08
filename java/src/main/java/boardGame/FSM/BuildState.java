package boardGame.FSM;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Worker;
import boardGame.DataStructure.PositionInfo;

public class BuildState implements IState{
    public GameState state;

    @Override
    public void onEnter(GameState state) {
        this.state = state;
    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        GameBoard board = state.mode.getGameBoard();
        Worker thisWorker = board.findWorker(sourcePositionInfo);
        if(thisWorker != state.currentPlayer.currentWorker()) return false;

        boolean performSuccess = state.buildBuilding(state.currentPlayer, thisWorker, targetPosition);
        if(!performSuccess) return false;

        state.changeToNextPlayer();
        state.changeState(state.mode.getNextState(this));
        
        return true;

    }

    @Override
    public void onExit() {

    }

    @Override
    public String toString(){
        return "build";
    }

    @Override
    public void specialAction(String content, GameState state) {
    }
}
