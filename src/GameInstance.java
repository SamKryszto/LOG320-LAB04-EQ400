import java.util.ArrayList;

public class GameInstance {

    // liste des possibilités d'échiquier à partir des positions actuelles
    private ArrayList<GameInstance> children;
    private boolean gameOver;
    private int depth;
    private GameInstance parent;
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
        rate();
        //verifie si la partie est terminée et genere les enfants sinon
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
    // Determine un score pour la situation actuelle d'un échiquier && determine si le joueur a gagné
    // Idée: +1 pour chaque jeton collé de maxPlayer et -1 pour chaque jeton collé de l'autre
    public int rate(){
        this.gameOver = false;
        int rate = 0;
        return rate;
    }

    // TO DO
    // genere la liste des echiquiers possibles a partir du plateau courant (generateMovements)
    public ArrayList<GameInstance> generateChildren(){
        ArrayList<GameInstance> children = new ArrayList<GameInstance>();
        return children;
    }
    
}
