package boardGame;

import java.util.ArrayList;
import java.util.List;

import boardGame.Buildings.Building;
import boardGame.Buildings.BuildingUnit;
import boardGame.DataStructure.PositionInfo;
import boardGame.FSM.IState;
import boardGame.GameMode.GameMode;

public class GameState {

    public GameMode mode;
    public List<Player> players;
    public Player currentPlayer;
    private boolean lastActionSuccess;

    public IState currentState;

    /**
     * You have to pass a game mode to the game state
     * Or the game can not be ran
     * @param mode
     */
    public GameState(GameMode mode){
        this.mode = mode;
    }


    @Override
    public String toString() {
        return "{"
            + "\"currentPhase\": \"" + (currentState != null ? currentState.toString(): "null") + "\", "
            + "\"currentPlayerID\": " + (currentPlayer != null ? currentPlayerID() : "null") + ", "
            + "\"currentWorkerID\": " + (currentPlayer != null ? currentPlayer.currentWorkerID() : "null") + ", "
            + "\"boardInfo\": " + (mode!= null ? mode.getGameBoard().toString() : "null") + ", "
            + "\"playersInfo\": " + (players!= null ? players.toString() : "null") + ", "
            + "\"ActionSuccess\": " + lastActionSuccess  + ", "
            + "\"GameMode\": " + mode.toString()
            + "}";
    }

    public int currentPlayerID(){
        return players.indexOf(currentPlayer);
    }

    /**
     * x1 and y1 are targetposition
     * x2 and y2 are sourceposition
     * @param position
     * @return true if action is performed, false if denied
     */
    public boolean performAction(int x1, int y1, int x2, int y2){
        PositionInfo targetPosition = new PositionInfo(x1, y1);
        PositionInfo sourcePositionInfo = new PositionInfo(x2, y2);
        lastActionSuccess = false;

        boolean performSuccess = currentState.onAction(targetPosition, sourcePositionInfo);
        lastActionSuccess = performSuccess;

        return performSuccess;
    }

    public void specialAction(String content){
        currentState.specialAction(content, this);
        lastActionSuccess = true;
    }

    public void startGame(int playerNum){

        players = new ArrayList<Player>();

        //Initilize all players
        while(playerNum >0){
            playerNum --;
            players.add(new Player(mode.workerNubmer()));
        }

        currentPlayer = players.get(0);
        lastActionSuccess = true;
        
        changeState(mode.getInitialState());
    }

    public void changeState(IState newState){
        if(currentState != null) currentState.onExit();
        currentState = newState;
        currentState.onEnter(this);
        System.out.println(currentState.toString());
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * This will find the next player. 
     * If the current player is the last player in list
     * Will return the first player
     * @return the next player
     */
    public Player getNextPlayer(){
        Player toReturn = null;

        for(int i =0; i< players.size(); i++){
            if(players.get(i)!= currentPlayer) continue;
            
            if(i == players.size()-1) toReturn = players.get(0);
            else toReturn = players.get(+1);
            
            break;
        }

        return toReturn;
    }

    /**
     * change to the next player
     * can be seen as turn passing
     * @return
     */
    public Player changeToNextPlayer(){
        currentPlayer = getNextPlayer();
        return currentPlayer;
    }

    /**
     * This is called to land a worker into the game for the first time
     * @param worker
     * @param toLand
     * @return
     */
    public boolean initiallySetWorkerPosition(Player player, Worker worker, PositionInfo toLand){    
        if(player != currentPlayer) return false;

        //You can't land a worker on another one, at least for game start
        if(!mode.getGameBoard().isInBoard(toLand)) return false;
        if(mode.getGameBoard().isOccupied(toLand)) return false;

        //Else, we land the worker on the desired position
        worker.position().setXY(toLand.getX(), toLand.getY());
        mode.getGameBoard().addWorker(worker);

        return true;
    }


    /**
     * This shall be called to move a walker that's already on board
     * @param worker
     * @param toMove
     * @return
     */
    public boolean moveWorker(Player player, Worker worker, PositionInfo toMove){
        if(player != currentPlayer) return false;

        //Ask game mode if that's a valide position
        if(!mode.validateMove(worker, toMove)) return false;

        //Else, we land the move the worker to the desired position
        worker.position().setXY(toMove.getX(), toMove.getY());

        return true;
    }


    /**
     * This is called to ask a worker to build a building in a position
     * @param worker
     * @param toBuild
     * @return
     */
    public boolean buildBuilding(Player player, Worker worker, PositionInfo toBuild){
        if(player != currentPlayer) return false;

        //Ask game mode for the kind of building we can build
        int buildId = mode.validateBuild(worker, toBuild);
        //-1 means not legal action
        if(buildId <0) return false;
        
        //Else, we build it
        Building building = mode.getGameBoard().findBuilding(toBuild);
        BuildingUnit unit = mode.getBuildingPool().generateUnit(buildId);
        building.build(unit);

        return true;
    }


}
