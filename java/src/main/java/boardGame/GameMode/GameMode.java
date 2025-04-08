package boardGame.GameMode;

import boardGame.GameBoard;
import boardGame.Player;
import boardGame.Worker;
import boardGame.Buildings.BuildingFactory;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.IState;


/**
 * Game Modes are responsible for validating some core game logic
 * It shouldn't change any state of the game
 * While it's a delegation to be used by Game State
 * It also holds object like GameBoard and Building Pool as part of the rule set of this mode
 */
public interface GameMode {

    public boolean validateMove(Worker worker, PositionInfo moveTo);

    public int validateBuild(Worker worker, PositionInfo buildAt);

    public boolean checkPlayerVictory(Player player);

    public GameBoard getGameBoard();

    public BuildingFactory getBuildingPool();

    public int workerNubmer();

    public IState getInitialState();

    public IState getNextState(IState currentState);
}
