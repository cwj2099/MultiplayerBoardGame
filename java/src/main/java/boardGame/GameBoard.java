package boardGame;

import java.util.ArrayList;
import java.util.List;

import boardGame.Buildings.Building;
import boardGame.Buildings.Dome;
import boardGame.DataStructure.PositionInfo;

public class GameBoard {
    private int xSize;
    private int ySize;
    private Building[][] buildings;
    private List<Worker> workers;

    @Override
    public String toString() {
        // toReturn += "]";
        String toReturn = "{";
        toReturn += "\"x\":" + xSize +",";
        toReturn += "\"y\":" + ySize +",";
        toReturn += "\"buildings\":" + "[";

        boolean added = false;
        for(int i = 0; i < xSize; i ++){
            for(int j = 0; j < ySize; j ++){
                //we only count those higher than 0
                Building building = buildings[i][j];
                if(building.height() == 0) continue;
                
                if(!added) added = true;
                else toReturn += ",";

                String buildingString = "{";
                buildingString += "\"x\":" + i +",";
                buildingString += "\"y\":" + j +",";
                buildingString += "\"units\":" + building.toString();
                buildingString += "}";
                toReturn += buildingString;
            }
        }
        toReturn +="]";
        toReturn += "}";
        return toReturn;
    }
    /**
     * GameBoard must be constructed with a size
     */
    public GameBoard(int x, int y){
        xSize = x;
        ySize = y;

        //Initialize all the buildings as well
        buildings = new Building[xSize][ySize];
        for(int i = 0; i< xSize; i++){
            for(int j = 0; j< ySize; j++){
                buildings[i][j] = new Building();
            }
        }

        //Initialize the worker container
        workers = new ArrayList<Worker>();
    }

    /**
     * Return the building at the given position
     * @param info
     * @return should always reeturn something
     */
    public Building findBuilding(PositionInfo info) {
        return buildings[info.getX()][info.getY()];
    }

    /**
     * Find if a worker is at the given position
     * @param info
     * @return might be null if no worker stands there
     */
    public Worker findWorker(PositionInfo info) {
        for (Worker worker : workers) {
            if(worker.position().samePos(info)) return worker;
        }
        return null;
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }


    /**
     * Check if there is a worker or a dome at the position
     * @param info
     * @return
     */
    public boolean isOccupied(PositionInfo info) {
        if(findWorker(info) != null) return true;
        if(findBuilding(info).currentTop() instanceof Dome) return true; 
        return false;
    }

    public int getXSize() {
        return xSize;
    }


    public int getYSize() {
        return ySize;
    }

    public boolean isInBoard(PositionInfo info) {
        if(info.getX() >= xSize) return false;
        if(info.getY() >= ySize) return false;
        if(info.getX() <0) return false;
        if(info.getY() <0) return false;
        return true;
    }
}
