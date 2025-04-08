package boardGame;

import java.io.IOException;
import java.util.Map;

import boardGame.Buildings.BuildingFactory;
import boardGame.GameMode.GameMode;
import boardGame.GameMode.GodCardMode;
import boardGame.GameMode.MinimalMode;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private GameState gameState;
    private GameMode normalMode;
    private GodCardMode godMode;
    private GameBoard gameBoard;
    private BuildingFactory buildFactory;

    /**
     * Start the server at :8080 port.
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    void initializeNormalGame(){
        this.gameBoard = new GameBoard(5, 5);
        this.buildFactory = new BuildingFactory();
        this.normalMode = new MinimalMode(gameBoard, buildFactory);
        this.gameState = new GameState(normalMode);
    }

    void initializeGodGame(){
        this.gameBoard = new GameBoard(5, 5);
        this.buildFactory = new BuildingFactory();
        this.godMode = new GodCardMode(gameBoard, buildFactory);
        this.gameState = new GameState(godMode);
        godMode.updateGodCards(0,"", gameState);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/newGame")) {
            initializeNormalGame();
            gameState.startGame(2);
        }
        else if (uri.equals("/newGodGame")){
            initializeGodGame();
            gameState.startGame(2);
        } 
        else if (uri.equals("/play")) {
            // e.g., /play?x=1&y=1
            //this.game = this.game.play(Integer.parseInt(params.get("x")), Integer.parseInt(params.get("y")));
            if(gameState == null) return newFixedLengthResponse("");

            gameState.performAction(Integer.parseInt(params.get("x1")), 
            Integer.parseInt(params.get("y1")), 
            Integer.parseInt(params.get("x2")), 
            Integer.parseInt(params.get("y2")));
        }
        else if (uri.equals("/getGod")){
            if(gameState == null) return newFixedLengthResponse("");
            return newFixedLengthResponse(godMode.godList());
        }
        else if (uri.equals("/special")){
            gameState.specialAction(params.get("content"));
        }
        else if (uri.equals("/currentGod")){
            if(gameState == null) return newFixedLengthResponse("");
            return newFixedLengthResponse(godMode.currentGod());
        }
        
        if(gameState == null) return newFixedLengthResponse("{}");

        // Extract the view-specific data from the game and apply it to the template.
        return newFixedLengthResponse(gameState.toString());
    }

}