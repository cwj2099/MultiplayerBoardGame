package boardGame.GameMode.GodCards;

import boardGame.FSM.BuildState;
import boardGame.FSM.IState;
import boardGame.FSM.InitializationState;
import boardGame.FSM.GodState.MinotaurMove;

public class Minotaur extends GodCard {

    @Override
    public IState getNexState(IState currentState) {

        if(currentState instanceof BuildState){
            return new MinotaurMove();
        }

        if(currentState instanceof InitializationState){
            return new MinotaurMove();
        }

        return null;
    }
   
    @Override
    public String name(){
        return "Minotaur";
    }
}
