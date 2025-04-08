package boardGame;

import boardGame.DataStructure.PositionInfo;

public class Worker {
    private PositionInfo pos;
    protected Player owner;

    @Override
    public String toString(){
        if(pos == null) return "";
        return "{"
        + "\"x\": " + pos.getX() + ","
        + "\"y\": " + pos.getY()
        + "}";
    }
    /**
     * The basic worker construction class
     * The position will be (-1,-1) by default which means they are not on board yet!
     */
    public Worker(){
        pos = new PositionInfo(-1, -1);
    }

    public Player getOwner(){
        return owner;
    }

    /**
     * Construct a worker and give it a initial position
     * @param x
     * @param y
     */
    public Worker(int x, int y, Player owner){
        pos = new PositionInfo(x, y);
        this.owner = owner;
    }

    /**
     * Construct a worker with positionInfo
     */
    public Worker(PositionInfo info, Player owner){
        pos = new PositionInfo(info.getX(), info.getY());
        this.owner = owner;
    }
    
    public Worker(Player owner){
        this.owner = owner;
        pos = new PositionInfo(-1, -1);
    }

    /**
     * You can directly modify the worker position information with this
     * Since it returns the reference of the object
     * @return
     */
    public PositionInfo position(){
        return pos;
    }

    /**
     * A easy way to update worker's position
     * @param info
     */
    public void setPosition(PositionInfo info){
        pos.setXY(info.getX(), info.getY());
    }
}
