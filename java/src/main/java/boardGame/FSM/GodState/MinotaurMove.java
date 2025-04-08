package boardGame.FSM.GodState;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Worker;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.EndState;
import boardGame.FSM.MoveState;

public class MinotaurMove extends MoveState{
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
        Worker otherWorker = board.findWorker(targetPosition);
        
        if(thisWorker == null) return false;
        if(thisWorker.getOwner() != state.currentPlayer) return false;
        
        //logic of pushing another player's worker
        if(otherWorker != null){
            //not pushing my self
            if(otherWorker.getOwner() == state.currentPlayer) return false;
            int difX = targetPosition.getX() - sourcePositionInfo.getX();
            int difY = targetPosition.getY() - sourcePositionInfo.getY();

            PositionInfo newPos = new PositionInfo( targetPosition.getX() + difX, targetPosition.getY() + difY);
            //not pushing for invalid destination
            if(!board.isInBoard(newPos)) return false;
            if(board.isOccupied(newPos)) return false;

            //pushing
            otherWorker.position().setXY(newPos.getX(), newPos.getY());
            //if not valid move, retreat the push
            if(!state.mode.validateMove(thisWorker, targetPosition)){
                otherWorker.position().setXY(targetPosition.getX(), targetPosition.getY());
                return false;
            }
        }


        boolean performSuccess = state.moveWorker(state.currentPlayer, thisWorker, targetPosition);
        if(!performSuccess) return false;

        state.currentPlayer.setCurrrentWorker(thisWorker);
        
        //Check self Victory
        if(state.mode.checkPlayerVictory(state.currentPlayer)){
            state.changeState(new EndState());
            return performSuccess;
        }

        //Check Opponent Victory
        if(state.mode.checkPlayerVictory(state.getNextPlayer())){
            state.changeToNextPlayer();
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
        return "MinotaurMove";
    }
}
