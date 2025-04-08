package boardGame.Buildings;

import java.util.Stack;


/**
 * Current the building is literally just a stack
 * But we might add additional functionality to it on need in future
 */
public class Building {
    private Stack<BuildingUnit> units;

    @Override
    public String toString(){
        return units.toString();
    }

    public Building(){
        units = new Stack<BuildingUnit>();
    }

    public BuildingUnit currentTop(){
        if(units.empty()) return null;
        return units.peek();
    }

    public void build(BuildingUnit unit){
        units.push(unit);
    }

    public int height(){
        return units.size();
    }
}
 