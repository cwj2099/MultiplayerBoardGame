package boardGame.GameMode.GodCards;

import boardGame.FSM.BuildState;
import boardGame.FSM.IState;
import boardGame.FSM.InitializationState;
import boardGame.FSM.GodState.PanMove;

public class Pan extends GodCard {

    @Override
    public IState getNexState(IState currentState) {

        if(currentState instanceof BuildState){
            return new PanMove();
        }

        if(currentState instanceof InitializationState){
            return new PanMove();
        }

        return null;
    }
 
    @Override
    public String name(){
        return "Pan";
    }
}