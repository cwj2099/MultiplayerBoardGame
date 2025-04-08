package boardGame;

import java.util.List;
import java.util.ArrayList;;

public class Player {
    private List<Worker> workers;
    private int currentWorker = 0;

    @Override
    public String toString(){
        if(workers == null) return "";
        return "{" + "\"workers\":" + workers.toString() + "}";
    }
    /**
     * Construct the player and give it the number of workers as you want
     * @param num
     */
    public Player(int num){
        workers = new ArrayList<Worker>();
        while(num >0){
            num--;
            workers.add(new Worker(this));
        }
    }

    public List<Worker> getAllWorkers(){
        return workers;
    }

    public void changeToNextWorker(){
        currentWorker ++;
        if(currentWorker >= workers.size()){
            currentWorker = 0;
        }
    }

    public Worker currentWorker(){
        return workers.get(currentWorker);
    }

    public int currentWorkerID(){
        return currentWorker;
    }

    public void setCurrrentWorker(Worker worker){
        if(!isPlayersWorker(worker)) return;
        currentWorker = workers.indexOf(worker);
    }

    public boolean isPlayersWorker(Worker worker){
        for (Worker w : workers) {
            if(w == worker) return true;
        }

        return false;
    }
    
}
