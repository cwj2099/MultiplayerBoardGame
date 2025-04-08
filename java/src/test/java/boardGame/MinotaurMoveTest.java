package boardGame;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardGame.Buildings.Building;
import boardGame.Buildings.Dome;
import boardGame.DataStructure.PositionInfo;
import boardGame.GameMode.GodCardMode;

public class MinotaurMoveTest {
    GameState state;
    GodCardMode mode;

    @BeforeEach
    public void initialization(){
        mode = new GodCardMode();
        state = new GameState(mode);

        state.startGame(2);
        state.specialAction("Minotaur");
        state.specialAction("Minotaur");
        state.performAction(2, 2, 0, 0);
        state.performAction(2, 3, 0, 0);
        state.performAction(3, 3, 0, 0);
        state.performAction(3, 2, 0, 0);
    }

    /**
     * test if can push opponent away when allow
     */
    @Test
    public void testNormalPush(){
        assertTrue(state.performAction(3, 2, 2, 2));
    }

    /**
     * test if can not push self chess
     */
    @Test
    public void tesNoSelfPush(){
        assertFalse(state.performAction(2, 3, 2, 2));
    }

    /**
     * test if can not push opponent if the next space is occupied
     */
    @Test
    public void testIllegalPush(){
        Building building = mode.getGameBoard().findBuilding(new PositionInfo(4, 2));
        building.build(new Dome());
        assertFalse(state.performAction(3, 2, 2, 2));
    }
}
