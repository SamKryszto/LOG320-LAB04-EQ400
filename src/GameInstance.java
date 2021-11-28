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
    private boolean tourDeBlanc; //true = blanc , false = noir
    private boolean gameOver;
    private int depth;
    private GameInstance parent;
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
        // A revoir: quand generer les enfants?
    }

    //temp
    /* 
    public GameInstance(GameInstance parent, ArrayList<GameInstance> children, int score, int depth){
        this.parent = parent;
        this.children = children;
        this.score = score;
        this.depth = depth;
    }
    */

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public GameInstance(int[][] grid, boolean tourDeBlanc, GameInstance parent, int nbBlancs, int nbNoirs, String lastMove) {
        this.grid = grid;
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
    public void generateChildren() {
        long bitBoardAllie;
        long bitBoardAdverse;
        long jetonsAlliesDansLigne;
        long jetonsAdversesDansLigne;
        int nombreJetonsDansLigne;
        int deplacementDansLigne;
        int caseDestination;
        long[] newBitBoards; 
        int newNbJetonsBlancs = nbBlancs;
        int newNbJetonsNoirs = nbNoirs;
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

    private String generateLastMove(int r1, int c1, int r2, int c2){
        return ""+ (char)(65 + c1) + (r1 + 1) + (char)(65 + c2) + (r2 + 1);
    }

    public String getLastMoveString(){
        return this.lastMove;
    }
}
