package boardGame.GameMode.GodCards;

import boardGame.Player;
import boardGame.Worker;
import boardGame.FSM.IState;
import boardGame.FSM.MoveState;
import boardGame.FSM.GodState.DemeterBuild;

public class Demeter extends GodCard {
    Player currentPlayer = null;
    Worker currentWorker = null;

    @Override
    public IState getNexState(IState currentState) {
        if(currentState instanceof MoveState){
            return new DemeterBuild();
        }
        return null;
    }

    @Override
    public String name(){
        return "Demeter";
    }
}
