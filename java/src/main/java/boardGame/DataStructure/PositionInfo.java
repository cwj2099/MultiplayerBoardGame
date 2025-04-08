package boardGame.DataStructure;

/**
 * This is a data record for the position of worker and building
 * Might be modified for future need if you want a 3D game
 * This class also have some constrain of X Y value base on design needs
 */
public class PositionInfo {
    private int x;
    private int y;

    //The constructor self call to apply the constrain
    public PositionInfo(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if(x < 0) this.x = 0;
        else this.x = x;
    }

    public void setY(int y) {
        if(y < 0) this.y = 0;
        else this.y = y;
    }

    public void setXY(int x, int y){
        setX(x);
        setY(y);
    }

    //A helper for comparing two position
    public boolean samePos(PositionInfo other){
        if(this.x != other.getX()) return false;
        if(this.y != other.getY()) return false;
        return true;
    }

    /**
     * A friendly helper that tells you if two position is adjacent
     * @param other
     * @return
     */
    public boolean adjacent(PositionInfo other){
        if(Math.abs(other.getX() - x) > 1) return false;
        if(Math.abs(other.getY() - y) > 1) return false;
        return true;
    }
}
