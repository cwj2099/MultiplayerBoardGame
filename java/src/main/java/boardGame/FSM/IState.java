package boardGame.FSM;

import boardGame.DataStructure.PositionInfo;
import boardGame.GameState;

public interface IState {

    public void onEnter(GameState state);

    public boolean onAction(PositionInfo targetPosition, PositionInfo sourcePositionInfo);

    public void onExit();

    public void specialAction(String content, GameState state);
}