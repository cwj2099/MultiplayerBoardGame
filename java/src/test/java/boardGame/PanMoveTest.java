package boardGame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardGame.Buildings.Block;
import boardGame.Buildings.Building;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.EndState;
import boardGame.GameMode.GodCardMode;

public class PanMoveTest {
        GameState state;
    GodCardMode mode;

    @BeforeEach
    public void initialization(){
        mode = new GodCardMode();
        state = new GameState(mode);

        state.startGame(2);
        state.specialAction("Pan");
        state.specialAction("Pan");
        state.performAction(2, 2, 0, 0);
        state.performAction(2, 3, 0, 0);
        state.performAction(3, 3, 0, 0);
        state.performAction(3, 2, 0, 0);
    }

    /**
     * test if can win from level 2 to ground
     */
    @Test
    public void testSpecialVictory(){
        Building building = mode.getGameBoard().findBuilding(new PositionInfo(2, 2));
        building.build(new Block(1));
        building.build(new Block(2));
        state.performAction(1, 2, 2, 2);

        assertTrue(state.currentState instanceof EndState);
        assertTrue(state.currentPlayerID() == 0);
    }

}
