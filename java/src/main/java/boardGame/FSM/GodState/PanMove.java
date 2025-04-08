package boardGame.FSM.GodState;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Worker;
import boardGame.Buildings.Building;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.EndState;
import boardGame.FSM.MoveState;

public class PanMove extends MoveState{
    public GameState state;
    
    boolean builtOnce;
    PositionInfo lastBuiltPos;

    @Override
    public void onEnter(GameState state) {
        this.state = state;
        builtOnce = false;
    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        GameBoard board = state.mode.getGameBoard();
        Worker thisWorker = board.findWorker(sourcePositionInfo);
        
        if(thisWorker == null) return false;
        if(thisWorker.getOwner() != state.currentPlayer) return false;

        boolean performSuccess = state.moveWorker(state.currentPlayer, thisWorker, targetPosition);
        if(!performSuccess) return false;

        state.currentPlayer.setCurrrentWorker(thisWorker);

        //Check self Victory
        if(state.mode.checkPlayerVictory(state.currentPlayer)){
            state.changeState(new EndState());
            return performSuccess;
        }

        //Check for special Victory
        Building lastBuilding = board.findBuilding(sourcePositionInfo);
        Building curreBuilding = board.findBuilding(targetPosition);
        if(lastBuilding.height() - curreBuilding.height() >= 2){
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
        return "PanMove";
    }

    
}
