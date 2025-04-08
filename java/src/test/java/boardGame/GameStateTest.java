package boardGame;

import org.junit.jupiter.api.Test;

import boardGame.Buildings.Building;
import boardGame.Buildings.BuildingFactory;
import boardGame.Buildings.Dome;
import boardGame.DataStructure.PositionInfo;
import boardGame.GameMode.GameMode;
import boardGame.GameMode.MinimalMode;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class GameStateTest {
  private GameState state;
  private GameMode mode;
  private GameBoard board;

  @BeforeEach
  public void initialization(){
    board = new GameBoard(5, 5);
    mode = new MinimalMode(board, new BuildingFactory());
    state = new GameState(mode);
    state.startGame(2);
  }
    
  
  /**
   * Starting a game should generate player properly
   */
  @Test
  public void testStartGameProperly() {
    assertTrue(state.getCurrentPlayer() != null,
    "start game might not setup properly");
  }

  /**
   * Next Player will return a different player if player num is more than one
   */
  @Test
  public void testGetNextPlayerProperly(){
    assertTrue((state.getCurrentPlayer() != state.getNextPlayer())
    ,"next player is not passed properly");
  }


  //---The Following are all intergation Tests---//


  /**
   * This test if we can set worker initial position properly
   */
  @Test
  public void testInitiallySetWorkerPosition(){
    Player player1 = state.getCurrentPlayer();
    Worker worker1 = player1.getAllWorkers().get(0);
    Worker worker2 = player1.getAllWorkers().get(1);

    //Test if we can move worker to a empty space
    assertTrue(state.initiallySetWorkerPosition(player1, worker1, new PositionInfo(0, 0))
    ,"Worker shall be able to land in a empty place");
    
    //See if work actually land there
    assertTrue(board.findWorker(new PositionInfo(0, 0)) == worker1);

    //Test if we can't place worker to a occupied place
    assertTrue(state.initiallySetWorkerPosition(player1, worker2, new PositionInfo(0, 0)) == false
    ,"Worker shouldn't land on another worker at start");

    //Test if we can't place work out of board
    assertTrue(state.initiallySetWorkerPosition(player1, worker2, new PositionInfo(-1, -1)) == false
    ,"Worker shouldn't land off board on start");
  }


  /**
   * This test the move worker with both valid and invalid action
   */
  @Test
  public void testMoveWorker(){
    Player player1 = state.getCurrentPlayer();
    Player player2 = state.getNextPlayer();
    Worker worker1 = player1.getAllWorkers().get(0);
    Worker worker2 = player2.getAllWorkers().get(0);

    worker1.setPosition(new PositionInfo(0,0));
    worker2.setPosition(new PositionInfo(0,1));
    board.addWorker(worker1);
    board.addWorker(worker2);

    //Test for valid action
    assertTrue(state.moveWorker(player1, worker1, new PositionInfo(1,0))
    ,"This should be a valid move, validation might be wrong");
    assertTrue(worker1.position().samePos(new PositionInfo(1,0))
    ,"After valid move, position should be updated");

    //Test for invalid action
    assertTrue(state.moveWorker(player2, worker2, new PositionInfo(1,0)) == false
    ,"This should be an invalid move, validation might be wrong");
    assertTrue(worker2.position().samePos(new PositionInfo(0,1))
    ,"After invalid move, position should not be updated");
  }

  /**
   * This test the build building with both valid and invalid action
   */
  @Test
  public void testBuildBuilding(){
    Player player1 = state.getCurrentPlayer();
    Player player2 = state.getNextPlayer();
    Worker worker1 = player1.getAllWorkers().get(0);
    Worker worker2 = player2.getAllWorkers().get(0);
    Building build1 = board.findBuilding(new PositionInfo(1,0));
    Building build2 = board.findBuilding(new PositionInfo(1,1));

    worker1.setPosition(new PositionInfo(0,0));
    worker2.setPosition(new PositionInfo(0,1));
    board.addWorker(worker1);
    board.addWorker(worker2);

    //Test for valid action
    assertTrue(state.buildBuilding(player1, worker1, new PositionInfo(1,0))
    ,"This should be a valid build, validation might be wrong");
    assertTrue(build1.height() == 1 ,"After valid build, building should be higher");
    
    state.buildBuilding(player1, worker1, new PositionInfo(1,0));
    state.buildBuilding(player1, worker1, new PositionInfo(1,0));
    state.buildBuilding(player1, worker1, new PositionInfo(1,0));
    assertTrue(build1.currentTop() instanceof Dome,"After 4 valid build, building should have dome");

    //Test for invalid action
    assertTrue(state.buildBuilding(player2, worker2, new PositionInfo(1,0)) == false
    ,"This should be an invalid build, validation might be wrong");
    assertTrue(build2.height() == 0,"After invalid move, building should not be updated");
  }
}
