package boardGame.FSM;

import boardGame.GameState;
import boardGame.DataStructure.PositionInfo;

public class EndState implements IState{
    public GameState state;

    @Override
    public void onEnter(GameState state) {
        this.state = state;
    }

    @Override
    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo) {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String toString(){
        return "end";
    }

    @Override
    public void specialAction(String content, GameState state) {
    }
}