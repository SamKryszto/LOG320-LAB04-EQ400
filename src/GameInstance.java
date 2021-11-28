import java.util.ArrayList;

public class GameInstance {

    // liste des possibilités d'échiquier à partir des positions actuelles
    private static ArrayList<GameInstance> children;
    private boolean tourDeBlanc; // true = blanc , false = noir
    private boolean gameOver;
    private int[][] grid;
    private int nbNoirs;
    private int nbBlancs;
    private String lastMove;
    private Joueur Jblanc;
    private Joueur Jnoir;
    private int score;

    // Échiquier initial
    public GameInstance() {
        this.tourDeBlanc = true;
        this.nbBlancs = 8;
        this.nbNoirs = 8;
        this.grid = new int[][] { { 0, 2, 2, 2, 2, 2, 2, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 2, 2, 2, 2, 2, 2, 0 } };
        this.gameOver = false;
        this.Jblanc = new Joueur(true, new ArrayList<Jeton>());
        this.Jnoir = new Joueur(false, new ArrayList<Jeton>());
        this.lastMove = "ERROR : First move";
        this.children = new ArrayList<GameInstance>();
        setJoueurJeton(grid);
        rate();
        // A revoir: quand generer les enfants?
    }

    // temp
    /*
     * public GameInstance(GameInstance parent, ArrayList<GameInstance> children,
     * int score, int depth){
     * this.parent = parent;
     * this.children = children;
     * this.score = score;
     * this.depth = depth;
     * }
     */

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public GameInstance(int[][] grid, boolean tourDeBlanc, int nbBlancs, int nbNoirs, String lastMove) {
        this.grid = grid;
        this.tourDeBlanc = tourDeBlanc;
        this.nbBlancs = nbBlancs;
        this.nbNoirs = nbNoirs;
        this.lastMove = lastMove;
        this.Jblanc = new Joueur(true, new ArrayList<Jeton>());
        this.Jnoir = new Joueur(false, new ArrayList<Jeton>());
        setJoueurJeton(grid);
        rate();
        // rate();
        // verifie si la partie est terminée et genere les enfants sinon
        if (!gameOver) {
            // A revoir: quand generer les enfants?
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
    public void rate() {
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
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int tempN = calculSquare(2, i, j);
                int tempB = calculSquare(4, i, j);
                if (tempN >= 4) {
                    maxNoir++;
                }
                if (tempB >= 4) {
                    maxBlanc++;
                }
            }
        }

        // Blanc doit toujours avec le plus de + grand nombre et noir le plus petit
        rate = maxBlanc - maxNoir;

        this.score = rate;
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

    public int findSquare(int joueur, int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7 || grid[row][col] != joueur) {
            return 0;
        }

        return 1;

    }

    public int calculSquare(int joueur, int row, int col) {

        if (findSquare(joueur, row, col) == 1) {
            if (findSquare(joueur, row, col + 1) == 1) {
                if (findSquare(joueur, row + 1, col + 1) == 1) {
                    if (findSquare(joueur, row + 1, col) == 1) {
                        return 4;
                    }
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    // TO DO
    // genere la liste des echiquiers possibles a partir du plateau courant
    // (generateMovements)
    public void generateChildren() {
        int jetonAllieID;
        int jetonAdverseID;

        // A qui est le tour - On attribue les jetons au joueur en cours
        int nbAdverses;
        if (this.tourDeBlanc) {
            jetonAllieID = 4;
            jetonAdverseID = 2;
            nbAdverses = nbNoirs;
        } else {
            jetonAllieID = 2;
            jetonAdverseID = 4;
            nbAdverses = nbBlancs;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Jetons dans chaque ligne et chaque diagonale
        int[] jetonsEnLigne = new int[8];
        int[] jetonsEnColonne = new int[8];

        // NOSE = Nord Ouest à Sud Est et SONE = Sud Ouest à Nord Est // L = ligne C =
        // Colonne
        int[] jetonsEnDiagNOSEL = new int[8];
        int[] jetonsEnDiagSONEL = new int[8];
        int[] jetonsEnDiagNOSEC = new int[8];
        int[] jetonsEnDiagSONEC = new int[8];
        int[][] tabDiagNOSE = new int[8][8];
        int[][] tabDiagSONE = new int[8][8];

        // NO - SE - L
        for (int i = 0; i < 8; i++) {
            int temp = 0;
            int c = 0;
            for (int j = i; j < 8; j++) {
                if (grid[j][c] == 2 || grid[j][c] == 4) {
                    temp++;
                }
                c++;
            }
            jetonsEnDiagNOSEL[i] = temp;
        }
        // fill tab
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= i; j++) {
                tabDiagNOSE[i][j] = jetonsEnDiagNOSEL[i - j];
            }
        }
        // NO - SE - C
        for (int i = 0; i < 8; i++) {
            int temp = 0;
            int r = 0;
            for (int j = i; j < 8; j++) {
                if (grid[r][j] == 2 || grid[r][j] == 4) {
                    temp++;
                }
                r++;
            }
            jetonsEnDiagNOSEC[i] = temp;
        }
        // fill tab
        for (int i = 0; i < 8; i++) {
            for (int j = i; j < 8; j++) {
                tabDiagNOSE[i][j] = jetonsEnDiagNOSEC[j - i];
            }
        }

        // SO - NE - C
        for (int i = 0; i < 8; i++) {
            int temp = 0;
            int r = i;
            for (int j = 0; j <= i; j++) {
                if (grid[j][r] == 2 || grid[j][r] == 4) {
                    temp++;
                }
                r--;
            }
            jetonsEnDiagSONEC[i] = temp;
        }

        // fill tab
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= i; j++) {
                tabDiagSONE[j][i - j] = jetonsEnDiagSONEC[i];
            }
        }

        // SO - NE - L
        for (int i = 0; i < 8; i++) {
            int temp = 0;
            int r = i;
            for (int j = 7; j >= i; j--) {
                if (grid[r][j] == 2 || grid[r][j] == 4) {
                    temp++;
                }
                r++;
            }
            jetonsEnDiagSONEL[i] = temp;
        }

        // fill tab
        for (int i = 0; i < 8; i++) {
            for (int j = 0; i + j < 8; j++) {
                tabDiagSONE[i + j][7 - j] = jetonsEnDiagSONEL[i];
            }
        }

        // Lines and columns
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (grid[r][c] == 2 || grid[r][c] == 4) {
                    jetonsEnLigne[r]++;
                    jetonsEnColonne[c]++;
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // pour chaque jeton du joueur actuel
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (grid[r][c] == jetonAllieID) {
                    int[] dirX;
                    int[] dirY;
                    if (c <= 0) {
                        dirX = new int[] { 0, 1 };
                    } else if (c < 7) {
                        dirX = new int[] { -1, 0, 1 };
                    } else {
                        dirX = new int[] { -1, 0 };
                    }

                    if (r <= 0) {
                        dirY = new int[] { 0, 1 };
                    } else if (r < 7) {
                        dirY = new int[] { -1, 0, 1 };
                    } else {
                        dirY = new int[] { -1, 0 };
                    }
                    int jetonsSurLaDirection = -2000;
                    String directionChoisie = "aucune";
                    // Pour chaque direction possible
                    for (int i = 0; i < dirX.length; i++) {
                        for (int j = 0; j < dirY.length; j++) {
                            int dirx = dirX[i];
                            int diry = dirY[j];

                            // System.out.println("dirX: " + dirX[i] + " dirY: " + dirY[j]);
                            // jetons sur la direction
                            if (!(dirX[i] == 0 && dirY[j] == 0)) {
                                if ((dirX[i] == 1 || dirX[i] == -1) && dirY[j] == 0) {
                                    jetonsSurLaDirection = jetonsEnLigne[r];
                                    directionChoisie = "Direction : O-E";
                                } else if ((dirY[j] == 1 || dirY[j] == -1) && dirX[i] == 0) {
                                    jetonsSurLaDirection = jetonsEnColonne[c];
                                    directionChoisie = "Direction : N-S";
                                } else if ((dirX[i] == -1 && dirY[j] == 1) || (dirX[i] == 1 && dirY[j] == -1)) {
                                    jetonsSurLaDirection = tabDiagSONE[r][c];
                                    directionChoisie = "Direction : SO-NE";
                                } else if ((dirX[i] == -1 && dirY[j] == -1) || (dirX[i] == 1 && dirY[j] == 1)) {
                                    jetonsSurLaDirection = tabDiagNOSE[r][c];
                                    directionChoisie = "Direction : NO-SE";

                                } else {
                                    System.out.println("Something's wrong...");
                                }
                                // System.out.println("Piece: R" + r + " C" + c + " " + directionChoisie
                                // + " Jetons en ligne: " + jetonsSurLaDirection);

                                boolean cheminLibre = true;
                                // Check si il y a des jetons adverses en chemin;
                                int newX;
                                int newY;
                                int step = 0;
                                do {
                                    newX = c + dirX[i] * step;
                                    newY = r + dirY[j] * step;
                                    if (newY < 8 && newY >= 0 && newX < 8 && newX >= 0) {
                                        if (grid[newY][newX] == jetonAdverseID) {
                                            cheminLibre = false;
                                        }
                                        step++;
                                    }
                                } while ((step < jetonsSurLaDirection) &&
                                        (newX < 8) &&
                                        (newX >= 0) &&
                                        (newY < 8) &&
                                        (newY >= 0) && cheminLibre);

                                // si chemin jusqu'à (pos + jetonsSurLaDirection) est libre (pas de jetons
                                // adverses)
                                // et si l'emplacement d'arrivée ne depasse pas l'échiquier
                                // et si l'emplacement d'arrivée n'est pas un jeton allié
                                newX += dirX[i];
                                newY += dirY[j];
                                if (cheminLibre &&
                                        (newX < 8) &&
                                        (newX >= 0) &&
                                        (newY < 8) &&
                                        (newY >= 0) &&
                                        grid[newY][newX] != jetonAllieID) {
                                    int newNbBlancs = nbBlancs;
                                    int newNbNoirs = nbNoirs;
                                    // si emplacement d'arrivée a un jeton adverse, on l'enlève.
                                    if (grid[newY][newX] == jetonAdverseID) {
                                        if (tourDeBlanc) {
                                            newNbNoirs--;
                                            Jnoir.retraitJeton(newX, newY);
                                        } else {
                                            newNbBlancs--;
                                            Jblanc.retraitJeton(newX, newY);
                                        }
                                    }
                                    // creer enfant, determiner parent, ajouter a la liste, attribuer last move
                                    int[][] childGrid = new int[8][8];
                                    for (int k = 0; k < 8; k++) {
                                        childGrid[k] = grid[k].clone();
                                    }
                                    childGrid[r][c] = 0;
                                    childGrid[newY][newX] = jetonAllieID;
                                    GameInstance enfant = new GameInstance(childGrid, !tourDeBlanc, newNbBlancs,
                                            newNbNoirs, generateLastMove(r, c, newX, newY));

                                    children.add(enfant);

                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getTurn() {
        return tourDeBlanc;
    }

    private String generateLastMove(int r1, int c1, int r2, int c2) {
        return "" + (char) (65 + c1) + (8 - r1) + (char) (65 + c2) + (8 - r2);
    }

    public String getLastMoveString() {
        return this.lastMove;
    }

    public String getNextMove(int score) {
        GameInstance child = null;
        System.out.println("Size : " + children.size());
        System.out.println("Score : " + score);
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getScore() == score) {
                child = children.get(i);
                System.out.println(child.getLastMoveString());
            }
        }
        System.out.println(child.getLastMoveString());
        return child.getLastMoveString();
    }
}
