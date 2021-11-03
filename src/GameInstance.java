import java.util.ArrayList;

public class GameInstance {

    // liste des possibilités d'échiquier à partir des positions actuelles
    private ArrayList<GameInstance> children;
    private boolean gameOver;
    private int[][] grid;
    private int nbNoirs;
    private int nbBlancs;

    // Échiquier initial
    public GameInstance(){
        this.nbBlancs = 8;
        this.nbNoirs = 8;
        this.grid = new int[][]{
            {0,2,2,2,2,2,2,0},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {0,2,2,2,2,2,2,0}
        };
        this.children = generateChildren();
        this.gameOver = false;
    }
    public GameInstance(int[][] grid){
        this.grid = grid;
        //verifie si la partie est terminée
        checkGameOver();
        if(!gameOver){
            this.children = generateChildren();
        }
    }

    public ArrayList<GameInstance> getChildren(){
        return this.children;
    }

    public boolean gameIsOver(){
        return this.gameOver;
    }

    // TO DO
    //verifie si la partie est terminée
    private void checkGameOver(){
    }

    // TO DO
    // genere la liste des echiquiers possibles (generateMovements)
    public ArrayList<GameInstance> generateChildren(){
        ArrayList<GameInstance> children = new ArrayList<GameInstance>();
        return children;
    }
    
}
