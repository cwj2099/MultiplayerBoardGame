package boardGame.GameMode.GodCards;

import boardGame.FSM.IState;
import boardGame.GameMode.GameMode;
import boardGame.GameState;

public abstract class GodCard {
    protected GameState state;
    protected GameMode mode;

    public void initialization(GameState state, GameMode mode){
        this.state = state;
        this.mode = mode;
    }

    public abstract IState getNexState(IState currentState);

    public abstract String name();
}