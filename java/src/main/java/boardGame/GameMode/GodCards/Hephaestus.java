package boardGame.GameMode.GodCards;

import boardGame.Player;
import boardGame.Worker;
import boardGame.FSM.IState;
import boardGame.FSM.MoveState;
import boardGame.FSM.GodState.HephaestusBuild;

public class Hephaestus extends GodCard {
    Player currentPlayer = null;
    Worker currentWorker = null;

    @Override
    public IState getNexState(IState currentState) {
        if(currentState instanceof MoveState){
            return new HephaestusBuild();
        }
        return null;
    }

    @Override
    public String name(){
        return "Hephaestus";
    }
    
}
