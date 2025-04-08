package boardGame.FSM;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Worker;
import boardGame.DataStructure.PositionInfo;

public class MoveState implements IState{
    public GameState state;

    @Override
    public void onEnter(GameState state) {
        this.state = state;
    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        GameBoard board = state.mode.getGameBoard();
        Worker thisWorker = board.findWorker(sourcePositionInfo);

        boolean performSuccess = state.moveWorker(state.currentPlayer, thisWorker, targetPosition);
        if(!performSuccess) return false;

        state.currentPlayer.setCurrrentWorker(thisWorker);

        //Break the sequence if game end
        if(state.mode.checkPlayerVictory(state.currentPlayer)){
            state.changeState(new EndState());
            return performSuccess;
        }


        state.changeState(state.mode.getNextState(this));
        return performSuccess;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String toString(){
        return "move";
    }

    @Override
    public void specialAction(String content, GameState state) {
    }
}
