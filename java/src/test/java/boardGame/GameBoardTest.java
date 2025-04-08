package boardGame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardGame.Buildings.Dome;
import boardGame.DataStructure.PositionInfo;

public class GameBoardTest {
    GameBoard board;
    PositionInfo position1;
    PositionInfo position2;

    @BeforeEach
    public void initialization(){
        position1 = new PositionInfo(0, 0);
        position2 = new PositionInfo(0, 1);
        board = new GameBoard(5, 5);
    }


    /**
     * If a worker's position is changed
     * It shall be reflected by find worker method
     */
    @Test
    public void testFindWorker(){
        Worker worker = new Worker(0,0,null);
        board.addWorker(worker);
        
        //currently worker shall be at position1
        assertTrue(board.findWorker(position1) == worker);
        assertTrue(board.findWorker(position2) != worker);

        //as we move, worker shall be at poistion2
        worker.setPosition(position2);
        assertTrue(board.findWorker(position1) != worker);
        assertTrue(board.findWorker(position2) == worker);
    }

    /**
     * If there is a work or dome at a point, return true
     * Else it shall be false
     */
    @Test
    public void testIsOccupired(){
        Worker worker = new Worker(0,0,null);
        Dome dome = new Dome();

        //nothing is built/stay so it shall be not occupied
        assertTrue(board.isOccupied(position1) == false);
        assertTrue(board.isOccupied(position2) == false);

        //something is there shall be occupied
        board.addWorker(worker);
        board.findBuilding(position2).build(dome);
        assertTrue(board.isOccupied(position1) == true);
        assertTrue(board.isOccupied(position2) == true);        
    }

    /**
     * something like 6,7 shouldn't be found in a map of 5 * 5
     */
    @Test
    public void testIsInBoard(){
        assertTrue(board.isInBoard(new PositionInfo(6,7)) == false);
    }
}
