package boardGame.FSM.GodState;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Worker;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.BuildState;

public class DemeterBuild extends BuildState{
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
        if(thisWorker != state.currentPlayer.currentWorker()) return false;

        if(!builtOnce){
            boolean performSuccess = state.buildBuilding(state.currentPlayer, thisWorker, targetPosition);
            if(!performSuccess) return false;

            builtOnce = true;
            lastBuiltPos = targetPosition;
            return true;
        }

        //for the second time
        //not allow for same space!
        if(targetPosition.samePos(lastBuiltPos)) return false;
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
    public void specialAction(String content, GameState state){

        if(content.equals("skip") && builtOnce){
            System.out.println(content);
            state.changeToNextPlayer();
            state.changeState(state.mode.getNextState(this));
        }
    }

    @Override
    public String toString(){
        return "DemeterBuild";
    }
}
