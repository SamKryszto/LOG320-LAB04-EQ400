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
    private Joueur Jblanc;
    private Joueur Jnoir;

    // temp
    private char column = 'A';
    private int row = 1;

    //temp
   private int score;

    // Échiquier initial
    public GameInstance() {
        this.nbBlancs = 8;
        this.nbNoirs = 8;
        this.grid = new int[][] { { 0, 2, 2, 2, 2, 2, 2, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 2, 2, 2, 2, 2, 2, 0 } };
        this.children = generateChildren();
        this.gameOver = false;
        this.Jblanc = new Joueur(true, new ArrayList<Jeton>());
        this.Jnoir = new Joueur(false, new ArrayList<Jeton>());

    }

    //temp
    public GameInstance(GameInstance parent, ArrayList<GameInstance> children, int score, int depth){
        this.parent = parent;
        this.children = children;
        this.score = score;
        this.depth = depth;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public GameInstance(int[][] grid) {
        this.grid = grid;
        rate();
        // verifie si la partie est terminée et genere les enfants sinon
        if (!gameOver) {
            this.children = generateChildren();
        }
    }

    public void setJoueurJeton(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 2) {
                    this.Jnoir.addJeton(new Jeton(i, j, false));
                }
                if (grid[i][j] == 4) {
                    this.Jblanc.addJeton(new Jeton(i, j, true));
                }
            }
        }
    }

    public ArrayList<GameInstance> getChildren() {
        return this.children;
    }

    public boolean gameIsOver() {
        return this.gameOver;
    }

    // TO DO
    // Determine un score pour la situation actuelle d'un échiquier && determine si
    // le joueur a gagné
    // Idée: +1 pour chaque jeton collé de maxPlayer et -1 pour chaque jeton collé
    // de l'autre #
    public int rate() {
        this.gameOver = false;
        int rate = 0;
        ArrayList<Jeton> listeJetonNoir = Jnoir.getListeJeton();
        int maxNoir = 0;
        for (Jeton j : listeJetonNoir) {
            int nbPieceN = nbPieceConnecte(2, j.getPosX(), j.getPosY(), new boolean[8][8]);
            if (maxNoir < nbPieceN) {
                maxNoir = nbPieceN;
            }
        }

        ArrayList<Jeton> listeJetonBlanc = Jblanc.getListeJeton();
        int maxBlanc = 0;
        for (Jeton j : listeJetonBlanc) {
            int nbPieceB = nbPieceConnecte(4, j.getPosX(), j.getPosY(), new boolean[8][8]);
            if (maxBlanc < nbPieceB) {
                maxBlanc = nbPieceB;
            }
        }

        rate = maxNoir - maxBlanc;
        return rate;
    }

    public void calculVictoire(int joueur, int row, int col) {
        if (joueur == 4) {
            if (nbPieceConnecte(joueur, row, col, new boolean[8][8]) == nbBlancs) {
                System.out.println("Victoire du joueur blanc !");
            }
        } else if (joueur == 2) {
            if (nbPieceConnecte(joueur, row, col, new boolean[8][8]) == nbNoirs) {
                System.out.println("Victoire du joueur noir !");
            }
        }
    }

    public int nbPieceConnecte(int joueur, int row, int col, boolean[][] verifiedGrid) {
        if (row < 0 || row > 7 || col < 0 || col > 7 || grid[row][col] != joueur || verifiedGrid[row][col]) {
            return 0;
        }
        verifiedGrid[row][col] = true;
        return 1 + nbPieceConnecte(joueur, row - 1, col - 1, verifiedGrid)
                + nbPieceConnecte(joueur, row - 1, col, verifiedGrid)
                + nbPieceConnecte(joueur, row - 1, col + 1, verifiedGrid)
                + nbPieceConnecte(joueur, row, col - 1, verifiedGrid)
                + nbPieceConnecte(joueur, row, col + 1, verifiedGrid)
                + nbPieceConnecte(joueur, row + 1, col + 1, verifiedGrid)
                + nbPieceConnecte(joueur, row + 1, col, verifiedGrid)
                + nbPieceConnecte(joueur, row + 1, col - 1, verifiedGrid);

    }

    // TO DO
    // genere la liste des echiquiers possibles a partir du plateau courant
    // (generateMovements)
    public ArrayList<GameInstance> generateChildren() {
        ArrayList<GameInstance> children = new ArrayList<GameInstance>();
        return children;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    //temp
    public int getScore() {
        return score;
    }
    
    /**
     * Cette méthode renvoie un String représentant le prochain mouvement de l'algorithme de jeu. 
     * 
     * Le mouvement à jouer doit être de format : "C1" + "R1" + "C2" + "R2" où C1 et R1 sont respectivement 
     * la lettre de colonne et le numéro de rangée initiales du pion à bouger et C2 et R2 sont respectivement 
     * la lettre de colonne et le numéro de rangée du pion après le mouvement.
     */
    public String getNextMove() {

        // À des fins de test, l'algorithme envoie pour l'instant une série de commandes peu stratégiques
        String nextMove = String.valueOf(column) + row + String.valueOf(column) + (row + 2);
        if (column != 'H') {
            column++;
        }
        else {
            column = 'A';
            row++;
        }
        

        return nextMove;
    }
}
