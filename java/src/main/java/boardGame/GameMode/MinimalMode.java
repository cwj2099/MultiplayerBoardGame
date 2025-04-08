package boardGame.GameMode;

import boardGame.GameBoard;
import boardGame.GameState;
import boardGame.Player;
import boardGame.Worker;
import boardGame.Buildings.Building;
import boardGame.Buildings.BuildingFactory;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.BuildState;
import boardGame.FSM.IState;
import boardGame.FSM.InitializationState;
import boardGame.FSM.MoveState;

public class MinimalMode implements GameMode{
    private GameBoard board;
    private BuildingFactory pool;

    /**
     * The default consturctor
     * Will set up the game mode with a 5 *5 board and Infinite pool
     */
    public MinimalMode(){
        board = new GameBoard(5, 5);
        pool = new BuildingFactory();
    }

    /**
     * The more custmoized constructor
     * In which you can choose your game board and building pool
     * @param board
     * @param pool
     */
    public MinimalMode(GameBoard board, BuildingFactory pool){
        this.board = board;
        this.pool = pool;
    }


    /**
     * This validation method check for following things
     * 1. is the worker moving to an adjacent point?
     * 2. is that point occupied already?
     * 3. is the level differences low enough?
     * 4. That need to be in boar
     */
    @Override
    public boolean validateMove(Worker worker, PositionInfo moveTo) {
        if(!board.isInBoard(moveTo)) return false;
        if(!worker.position().adjacent(moveTo)) return false;
        if(board.isOccupied(moveTo)) return false;
        //todo: shall I check if a worker is in board yet?
        
        Building current = board.findBuilding(worker.position());
        Building next = board.findBuilding(moveTo);
        if(next.height() - current.height() > 1) return false;

        return true;
    }


    /**
     * This validation method will check if a position is occupied
     * Or is it not adjacent
     * Or is it not out of bound
     * If not, it will return a index that indicate what shall be the next building to build
     */
    @Override
    public int validateBuild(Worker worker, PositionInfo buildAt) {
        if(!board.isInBoard(buildAt)) return -1;
        if(!worker.position().adjacent(buildAt)) return -1;
        if(board.isOccupied(buildAt)) return -1;

        Building target = board.findBuilding(buildAt);

        //If no units yet, build first level
        if(target.currentTop() == null) return 1;
        //If there is, built one more floor
        return target.height() + 1;
    }

    /**
     * We only check if any of the worker is on a level of three
     */
    @Override
    public boolean checkPlayerVictory(Player player) {

        for (Worker worker: player.getAllWorkers()) {
            Building place = board.findBuilding(worker.position());
            if(place.height() >= 3) return true;
        }
        
        return false;
    }


    @Override
    public GameBoard getGameBoard() {
        return board;
    }

    @Override
    public BuildingFactory getBuildingPool() {
        return pool;
    }

    @Override
    public int workerNubmer(){
        return 2;
    }


    @Override
    public String toString(){
        return "\"Normal\"";
    }

    @Override
    public IState getInitialState() {
        return new InitializationState();
    }

    @Override
    public IState getNextState(IState currentState) {
        
        if(currentState instanceof InitializationState){
            return new MoveState();
        }

        if(currentState instanceof MoveState){
            return new BuildState();
        }

        if(currentState instanceof BuildState){
            return new MoveState();
        }

        return new InitializationState();
    }
    
}
