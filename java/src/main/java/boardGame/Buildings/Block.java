package boardGame.Buildings;

public class Block implements BuildingUnit{
    private int level;

    public Block(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    @Override
    public String toString(){
        return "{"+ "\"info\": " + level +"}";
    }
}
