package boardGame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardGame.Buildings.Block;
import boardGame.Buildings.Building;
import boardGame.Buildings.BuildingFactory;
import boardGame.Buildings.Dome;
import boardGame.DataStructure.PositionInfo;
import boardGame.GameMode.GameMode;
import boardGame.GameMode.MinimalMode;

public class MinimalModeTest {
    private GameMode mode;
    private GameBoard board;
    private Worker worker1;
    private Worker worker2;

    @BeforeEach
    public void initialization(){
        board = new GameBoard(5, 5);
        mode = new MinimalMode(board, new BuildingFactory());

        worker1 = new Worker(0, 2, null);
        worker2 = new Worker(0, 3, null);

        board.addWorker(worker1);
        board.addWorker(worker2);
    }

    /**
     * This tests validate move specification
     * You need to satisfied all condition to move legally
     */
    @Test
    public void testValidateMove(){
        //you can not move to far way block
        assertTrue(mode.validateMove(worker1, new PositionInfo(0, 4)) == false
        ,"Worker can move to a not adjacent block when they shall not");

        //you can not move a place that is occupied
        assertTrue(mode.validateMove(worker1, new PositionInfo(0, 3)) == false
        ,"Worker can move to a place occupied by another worker when they shall not");

        //you can not move to a place too high
        Building build = board.findBuilding(new PositionInfo(0,1));
        build.build(new Block(1));
        build.build(new Block(2));
        assertTrue(mode.validateMove(worker1, new PositionInfo(0, 1)) == false
        ,"Worker can move to a place 2 building higher when they shall not");

        //you can not move out of board
        assertTrue(mode.validateMove(worker1, new PositionInfo(0, -1)) == false
        ,"Worker can some how move out of board when they shall not");

        //else, this place is fine to move
        assertTrue(mode.validateMove(worker1, new PositionInfo(1, 2)) == true
        ,"This place shall be movable but seen as not");
    }

    /**
     * This tests validate build specification
     * You need to satisfied all condition to build legally
     * Also it should return a proper index
     */
    @Test
    public void testValidateBuild(){

        Building build = board.findBuilding(new PositionInfo(0,1));

        //Check for valid build
        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, 1)) == 1,
        "Expect building on ground to be level1");

        build.build(new Block(1));
        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, 1)) == 2,
        "Expect building on 1 to be level2");

        build.build(new Block(2));
        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, 1)) == 3,
        "Expect building on 2 to be level3");

        //Check for occupied build
        build.build(new Block(3));
        build.build(new Dome());
        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, 1)) == -1,
        "Shall not be build at dome");

        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, 3)) == -1,
        "Shall not be able to build at a player pos ");

        //Check for out of bound build
        assertTrue(mode.validateBuild(worker1, new PositionInfo(0, -1)) == -1,
        "Shall not be able to build out of bound");

        //Check for not building at adjacent
        assertTrue(mode.validateBuild(worker1, new PositionInfo(4,4)) == -1,
        "Shall not be able to build farway");

    }
}
