package boardGame.Buildings;

public class BuildingFactory {


    public BuildingFactory(){
        
    }

    public Block generateBlock(int level) {
        return new Block(level);
    }

    public Dome generateDome() {
        return new Dome();
    }

    public BuildingUnit generateUnit(int level) {
        if(level < 4) return generateBlock(level);
        else return generateDome();
    }
}
