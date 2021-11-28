import java.util.ArrayList;

public class GameInstance {

    public static final int LONG_LENGTH = 64;
    public static final int BIT_BOARD_LENGTH = 63;
    public static final int S_N_SHIFT = 8;
    public static final int O_E_SHIFT = 1;
    public static final int SE_NO_SHIFT = 9;
    public static final int SO_NE_SHIFT = 7;




    // liste des possibilités d'échiquier à partir des positions actuelles
    private ArrayList<GameInstance> children;
    private GameInstance parent;
    private boolean tourDeBlanc; // true = blanc , false = noir
    private boolean gameOver;
    private int[][] grid;
    private int nbNoirs;
    private int nbBlancs;
    private String lastMove;
    private Joueur Jblanc;
    private Joueur Jnoir;
    private long bitBoardBlancs;
    private long bitBoardNoirs;


    // temp
    private char column = 'A';
    private int row = 1;

    //temp
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

    public GameInstance(int[][] grid, boolean tourDeBlanc, String lastMove) {
        this.grid = grid;
        this.tourDeBlanc = tourDeBlanc;
        this.lastMove = lastMove;
        this.Jblanc = new Joueur(true, new ArrayList<Jeton>());
        this.Jnoir = new Joueur(false, new ArrayList<Jeton>());
        this.children = new ArrayList<GameInstance>();
        setJoueurJeton(grid);
        this.nbBlancs = Jblanc.getListeJeton().size();
        this.nbNoirs = Jblanc.getListeJeton().size();

        /**
         * switch (calculVictoire()) {
         * case 0:
         * // Rien
         * case 1: // Victoire blanc
         * this.score = 1000;
         * this.gameOver = true;
         * case 2: // Victoire noir
         * this.score = -1000;
         * this.gameOver = true;
         * }
         */

    }

    public GameInstance(long bitBoardBlancs, long bitBoardNoirs, boolean tourDeBlanc, GameInstance parent, int nbBlancs, int nbNoirs, String lastMove) {
        this.bitBoardBlancs = bitBoardBlancs;
        this.bitBoardNoirs = bitBoardNoirs;
        this.tourDeBlanc = tourDeBlanc;
        this.parent = parent;
        this.nbBlancs = nbBlancs;
        this.nbNoirs = nbNoirs;
        this.lastMove = lastMove;
        this.children = new ArrayList<GameInstance>();
        //rate();
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

    public int calculVictoire() {

        if (nbPieceConnecte(4, Jblanc.getListeJeton().get(0).getPosX(), Jblanc.getListeJeton().get(0).getPosY(),
                new boolean[8][8]) == nbBlancs) {
            return 1;
        }

        if (nbPieceConnecte(2, Jnoir.getListeJeton().get(0).getPosX(), Jnoir.getListeJeton().get(0).getPosY(),
                new boolean[8][8]) == nbNoirs) {
            return 2;
        }

        return 0;
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
        long bitBoardAllie;
        long bitBoardAdverse;
        long jetonsAlliesDansLigne;
        long jetonsAdversesDansLigne;
        int nombreJetonsDansLigne;
        int deplacementDansLigne;
        int caseDestination;
        long[] newBitBoards; 
        int limiteLigne;
        int premiereCaseDansLigne;
        
        // A qui est le tour - On attribue les jetons au joueur en cours
        if(this.tourDeBlanc){
            bitBoardAllie = bitBoardBlancs;
            bitBoardAdverse = bitBoardNoirs;
        }
        else {
            bitBoardAllie = bitBoardNoirs;
            bitBoardAdverse = bitBoardBlancs;
        }

        // pour chaque ligne d'action du jeu

// /**************************************************************************************************************
//  *                                                  SUD_NORD
//  **************************************************************************************************************/ 
//         for (premiereCaseDansLigne = 0; premiereCaseDansLigne < 8; premiereCaseDansLigne ++){
            
           
//             // on récupère un bitBoard représentant les jetons alliés dans la ligne
//             //jetonsAlliesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAllie);
//             jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.S_N);
//             System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
//             // s'il y a des jetons alliés dans la ligne
//             if (jetonsAlliesDansLigne != 0) {
//                 // On récupère un bitboard représentant les jetons adverses dans la ligne
//                 //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
//                 jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.S_N);
//                 // on compte le nombre de jetons dans la ligne
//                 nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
//                 deplacementDansLigne = nombreJetonsDansLigne * 8;
//                 // pour chaque case dans la ligne
//                 for(int caseJeton = premiereCaseDansLigne; caseJeton < 63; caseJeton += 8) {
//                     // s'il y a un jeton allié dans la case
//                     if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
//                         caseDestination = caseJeton - deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((caseJeton >= deplacementDansLigne) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
                        
//                         // dans l'autre direction
//                         caseDestination = caseJeton + deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((63 >= caseDestination) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - (deplacementDansLigne - 1))) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
//                     }
//                 }
//             }
//         }


// /**************************************************************************************************************
//  *                                                  EST_OUEST
//  **************************************************************************************************************/ 
//         for (premiereCaseDansLigne = 0; premiereCaseDansLigne < 63; premiereCaseDansLigne += 8) {
//             // on récupère un bitBoard représentant les jetons alliés dans la ligne
//             jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.E_O);
//             System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
//             // s'il y a des jetons alliés dans la ligne
//             if (jetonsAlliesDansLigne != 0) {
//                 // On récupère un bitboard représentant les jetons adverses dans la ligne
//                 //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
//                 jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.E_O);
//                 // on compte le nombre de jetons dans la ligne
//                 nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
//                 deplacementDansLigne = nombreJetonsDansLigne;
//                 // pour chaque case dans la ligne
//                 limiteLigne = premiereCaseDansLigne + 7;
//                 for(int caseJeton = premiereCaseDansLigne; caseJeton <= limiteLigne; caseJeton ++) {
//                     // s'il y a un jeton allié dans la case
//                     if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
//                         caseDestination = caseJeton - deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((caseDestination >= premiereCaseDansLigne) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
                        
//                         // dans l'autre direction
//                         caseDestination = caseJeton + deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((limiteLigne >= caseDestination) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - deplacementDansLigne)) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
//                     }
//                 }
//             }
//         }
// /**************************************************************************************************************
//  *                                                  SUD-EST_NORD-OUEST
//  **************************************************************************************************************/ 
//         for (premiereCaseDansLigne = 0; premiereCaseDansLigne < 7; premiereCaseDansLigne++) {
//             // on récupère un bitBoard représentant les jetons alliés dans la ligne
//             jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.SE_NO);
//             System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
//             // s'il y a des jetons alliés dans la ligne
//             if (jetonsAlliesDansLigne != 0) {
//                 // On récupère un bitboard représentant les jetons adverses dans la ligne
//                 //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
//                 jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.SE_NO);
//                 // on compte le nombre de jetons dans la ligne
//                 nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
//                 deplacementDansLigne = nombreJetonsDansLigne * 9;
//                 // pour chaque case dans la ligne
//                 limiteLigne = 63 - 8 * premiereCaseDansLigne;
//                 for(int caseJeton = premiereCaseDansLigne; caseJeton <= limiteLigne; caseJeton += 9) {
//                     // s'il y a un jeton allié dans la case
//                     if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
//                         caseDestination = caseJeton - deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((caseDestination >= premiereCaseDansLigne) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
                        
//                         // dans l'autre direction
//                         caseDestination = caseJeton + deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((limiteLigne >= caseDestination) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - deplacementDansLigne)) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
//                     }
//                 }
//             }
//         }

//         for (premiereCaseDansLigne = 8; premiereCaseDansLigne < 63; premiereCaseDansLigne += 8) {
//             // on récupère un bitBoard représentant les jetons alliés dans la ligne
//             jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.SE_NO);
//             System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
//             // s'il y a des jetons alliés dans la ligne
//             if (jetonsAlliesDansLigne != 0) {
//                 // On récupère un bitboard représentant les jetons adverses dans la ligne
//                 //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
//                 jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.SE_NO);
//                 // on compte le nombre de jetons dans la ligne
//                 nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
//                 deplacementDansLigne = nombreJetonsDansLigne * 9;
//                 // pour chaque case dans la ligne
//                 limiteLigne = 63;
//                 for(int caseJeton = premiereCaseDansLigne; caseJeton <= limiteLigne; caseJeton += 9) {
//                     // s'il y a un jeton allié dans la case
//                     if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
//                         caseDestination = caseJeton - deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((caseDestination >= premiereCaseDansLigne) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
                        
//                         // dans l'autre direction
//                         caseDestination = caseJeton + deplacementDansLigne;
//                         // si la case n'est pas trop près du bord
//                         if ((limiteLigne >= caseDestination) && 
//                         // si la case d'arrivée n'est pas occupée par un jeton allié
//                         (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
//                         // si les cases intermédiaires ne sont pas occupées par un jeton adverse
//                         ((((-1L >>> (64 - deplacementDansLigne)) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
//                             // on ajoute un GameInstance enfant dans le GameInstance actuel
//                             newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
//                             if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
//                                 if (this.tourDeBlanc) {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, nbBlancs, (nbNoirs - 1), 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                                 else {
//                                     GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                             this, (nbBlancs - 1), nbNoirs, 
//                                                             Masks.getMovementCode(caseJeton, caseDestination));
//                                     children.add(enfant);
//                                 }
//                             }
//                             else {
//                                 GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
//                                                         this, nbBlancs, nbNoirs, 
//                                                         Masks.getMovementCode(caseJeton, caseDestination));
//                                 children.add(enfant);
//                             }
//                         }
//                     }
//                 }
//             }
//         }

/**************************************************************************************************************
 *                                                  SUD-OUEST_NORD-EST
 **************************************************************************************************************/ 
        for (premiereCaseDansLigne = 1; premiereCaseDansLigne < 8; premiereCaseDansLigne++) {
            // on récupère un bitBoard représentant les jetons alliés dans la ligne
            jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.SO_NE);
            System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
            // s'il y a des jetons alliés dans la ligne
            if (jetonsAlliesDansLigne != 0) {
                // On récupère un bitboard représentant les jetons adverses dans la ligne
                //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
                jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.SO_NE);
                // on compte le nombre de jetons dans la ligne
                nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
                deplacementDansLigne = nombreJetonsDansLigne * 7;
                // pour chaque case dans la ligne
                limiteLigne = premiereCaseDansLigne + 8 * premiereCaseDansLigne;
                for(int caseJeton = premiereCaseDansLigne; caseJeton < limiteLigne; caseJeton += 7) {
                    // s'il y a un jeton allié dans la case
                    if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
                        caseDestination = caseJeton - deplacementDansLigne;
                        // si la case n'est pas trop près du bord
                        if ((caseDestination >= premiereCaseDansLigne) && 
                        // si la case d'arrivée n'est pas occupée par un jeton allié
                        (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
                        // si les cases intermédiaires ne sont pas occupées par un jeton adverse
                        ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
                            // on ajoute un GameInstance enfant dans le GameInstance actuel
                            newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
                            if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
                                if (this.tourDeBlanc) {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, nbBlancs, (nbNoirs - 1), 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                                else {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, (nbBlancs - 1), nbNoirs, 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                            }
                            else {
                                GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                        this, nbBlancs, nbNoirs, 
                                                        Masks.getMovementCode(caseJeton, caseDestination));
                                children.add(enfant);
                            }
                        }
                        
                        // dans l'autre direction
                        caseDestination = caseJeton + deplacementDansLigne;
                        // si la case n'est pas trop près du bord
                        if ((limiteLigne >= caseDestination) && 
                        // si la case d'arrivée n'est pas occupée par un jeton allié
                        (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
                        // si les cases intermédiaires ne sont pas occupées par un jeton adverse
                        ((((-1L >>> (64 - deplacementDansLigne)) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
                            // on ajoute un GameInstance enfant dans le GameInstance actuel
                            newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
                            if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
                                if (this.tourDeBlanc) {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, nbBlancs, (nbNoirs - 1), 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                                else {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, (nbBlancs - 1), nbNoirs, 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                            }
                            else {
                                GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                        this, nbBlancs, nbNoirs, 
                                                        Masks.getMovementCode(caseJeton, caseDestination));
                                children.add(enfant);
                            }
                        }
                    }
                }
            }
        }

        for (premiereCaseDansLigne = 15; premiereCaseDansLigne < 63; premiereCaseDansLigne += 8) {
            // on récupère un bitBoard représentant les jetons alliés dans la ligne
            jetonsAlliesDansLigne = bitBoardAllie & Masks.getMask(premiereCaseDansLigne, Masks.SO_NE);
            System.out.println(Long.toBinaryString(jetonsAlliesDansLigne));
            // s'il y a des jetons alliés dans la ligne
            if (jetonsAlliesDansLigne != 0) {
                // On récupère un bitboard représentant les jetons adverses dans la ligne
                //jetonsAdversesDansLigne = Masks.getPiecesNToS(premiereCaseDansLigne, bitBoardAdverse);
                jetonsAdversesDansLigne = bitBoardAdverse & Masks.getMask(premiereCaseDansLigne, Masks.SO_NE);
                // on compte le nombre de jetons dans la ligne
                nombreJetonsDansLigne = countJetonsDansLigne(jetonsAlliesDansLigne | jetonsAdversesDansLigne);
                deplacementDansLigne = nombreJetonsDansLigne * 7;
                // pour chaque case dans la ligne
                limiteLigne = 63;
                for(int caseJeton = premiereCaseDansLigne; caseJeton < limiteLigne; caseJeton += 7) {
                    // s'il y a un jeton allié dans la case
                    if (((jetonsAlliesDansLigne >>> caseJeton) & 0b1) != 0) {
                        caseDestination = caseJeton - deplacementDansLigne;
                        // si la case n'est pas trop près du bord
                        if ((caseDestination >= premiereCaseDansLigne) && 
                        // si la case d'arrivée n'est pas occupée par un jeton allié
                        (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
                        // si les cases intermédiaires ne sont pas occupées par un jeton adverse
                        ((((-1L >>> (64 - (deplacementDansLigne - 1))) << (caseDestination + 1)) & jetonsAdversesDansLigne) == 0)) {
                            // on ajoute un GameInstance enfant dans le GameInstance actuel
                            newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
                            if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
                                if (this.tourDeBlanc) {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, nbBlancs, (nbNoirs - 1), 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                                else {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, (nbBlancs - 1), nbNoirs, 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                            }
                            else {
                                GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                        this, nbBlancs, nbNoirs, 
                                                        Masks.getMovementCode(caseJeton, caseDestination));
                                children.add(enfant);
                            }
                        }
                        
                        // dans l'autre direction
                        caseDestination = caseJeton + deplacementDansLigne;
                        // si la case n'est pas trop près du bord
                        if ((limiteLigne >= caseDestination) && 
                        // si la case d'arrivée n'est pas occupée par un jeton allié
                        (((jetonsAlliesDansLigne >>> (caseDestination)) & 0b1) == 0) &&
                        // si les cases intermédiaires ne sont pas occupées par un jeton adverse
                        ((((-1L >>> (64 - deplacementDansLigne)) << caseJeton) & jetonsAdversesDansLigne) == 0)) {
                            // on ajoute un GameInstance enfant dans le GameInstance actuel
                            newBitBoards = Masks.movePiece(caseJeton, caseDestination, bitBoardAllie,bitBoardAdverse);
                            if ((newBitBoards[1] & bitBoardAdverse) != bitBoardAdverse ) {
                                if (this.tourDeBlanc) {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, nbBlancs, (nbNoirs - 1), 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                                else {
                                    GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                            this, (nbBlancs - 1), nbNoirs, 
                                                            Masks.getMovementCode(caseJeton, caseDestination));
                                    children.add(enfant);
                                }
                            }
                            else {
                                GameInstance enfant = new GameInstance(newBitBoards[0], newBitBoards[1], !tourDeBlanc, 
                                                        this, nbBlancs, nbNoirs, 
                                                        Masks.getMovementCode(caseJeton, caseDestination));
                                children.add(enfant);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Méthode permettant de compter le nombre de bits à 1 dans un long utilisant 
     * l'algorithme de Brian Kernighan.
     */
    public int countJetonsDansLigne(long jetonsDansLigne) {
        int count = 0;

        while (jetonsDansLigne != 0) {
            jetonsDansLigne &= (jetonsDansLigne - 1);
            count++;
        }
        return count;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public long getBitBoardBlancs() {
        return bitBoardBlancs;
    }

    public long getBitBoardNoirs() {
        return bitBoardNoirs;
    }

    //temp
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
        return " " + (char) (65 + c1) + (8 - r1) + (char) (65 + c2) + (8 - r2);
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
                break;
            }
        }
        return child.getLastMoveString();
    }

    // temp
    public boolean compareGrids(int[][] gridToCompare) {
        boolean same = true;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (this.grid[i][j] != gridToCompare[i][j]) {
                    same = false;
                }
            }
        }
        return same;
    }
}
