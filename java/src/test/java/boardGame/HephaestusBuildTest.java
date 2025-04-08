package boardGame;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardGame.FSM.MoveState;
import boardGame.FSM.GodState.HephaestusBuild;
import boardGame.GameMode.GodCardMode;

public class HephaestusBuildTest {
    GameState state;
    GodCardMode mode;
    HephaestusBuild buildState;

    @BeforeEach
    public void initialization(){
        mode = new GodCardMode();
        state = new GameState(mode);

        buildState = new HephaestusBuild();

        state.startGame(2);
        state.specialAction("Hephaestus");
        state.specialAction("Hephaestus");
        state.performAction(2, 2, 0, 0);
        state.performAction(2, 3, 0, 0);
        state.performAction(3, 3, 0, 0);
        state.performAction(3, 2, 0, 0);

        //We skip the move state
        state.changeState(buildState);
    }

    /**
     * test if can not build in two different place
     */
    @Test
    public void testForNoDifferentBuild(){
        assertTrue(state.performAction(2, 1, 2, 2));
        assertFalse(state.performAction(1, 2, 2, 2));
    }

    /**
     * test if can build in the same place
     */
    @Test
    public void testForSameSpace(){
        assertTrue(state.performAction(2, 1, 2, 2));
        assertTrue(state.performAction(2, 1, 2, 2));
    }

    /**
     * test if can skip
     */
    @Test
    public void testForSkip(){
        assertTrue(state.performAction(2, 1, 2, 2));
        state.specialAction("skip");
        assertTrue(state.currentState instanceof MoveState);
    }
}
