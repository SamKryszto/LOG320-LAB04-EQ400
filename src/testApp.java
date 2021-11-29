import java.util.ArrayList;

public class testApp {
    public static void main(String[] args) throws Exception {
        int[][] gridInit = new int[][] { { 2, 2, 2, 2, 2, 2, 2, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 2, 2, 2, 2, 2, 2, 0 } };
        int[][] gridRando = new int[][] { { 0, 0, 2, 0, 2, 2, 0, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 0, 0, 0, 4, 0, 2, 0, 4 },
                { 4, 0, 0, 0, 2, 0, 0, 4 }, { 4, 2, 0, 0, 0, 0, 0, 4 }, { 4, 0, 2, 4, 0, 0, 0, 0 },
                { 0, 0, 4, 2, 0, 0, 0, 4 }, { 0, 2, 0, 0, 2, 2, 2, 0 } };

        // testFonctionFindSquare(new GameInstance());
        testGenerateChildren();
        // testFindJetonsInARow(gridRando);
        // testFonctionEval(new GameInstance());
        // testMinimax();

    }

    public static void testFonctionEval(GameInstance gameInstance) {
        int[][] grid = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 2, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 2, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 } };
        gameInstance.setGrid(grid);
        gameInstance.setJoueurJeton(grid);
    }

    public static void testFonctionFindSquare(GameInstance gameInstance) {
        int[][] grid = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 2, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 2, 0, 0 },
                { 0, 0, 0, 0, 0, 4, 4, 0 },
                { 0, 0, 0, 2, 2, 4, 4, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 } };
        gameInstance.setGrid(grid);
        gameInstance.setJoueurJeton(grid);

        // gameInstance.rate();

        int a = gameInstance.getScore();
        System.out.println("Score : " + a);

    }

    public static void testGenerateChildren() {
        GameInstance gameInit = new GameInstance();
        int[][] gridTest = new int[][] { { 0, 2, 2, 2, 2, 2, 2, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 4, 0, 0, 0, 0, 4, 0, 0 },
                { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 2, 2, 2, 2, 2, 2, 0 } };
        GameInstance gameTest = new GameInstance(gridTest, false, null);
        gameTest.generateChildren();
        // printGrid(gameTest.getGrid());
        ArrayList<GameInstance> children = gameTest.getChildren();
        System.out.println("Size :" + children.size());
        for (int i = 0; i < children.size(); i++) {
            System.out.println(children.get(i).getLastMoveString());
            System.out.println("Score : " + children.get(i).getScore());
            printGrid(children.get(i).getGrid());
        }

        // String a = app.getBestMoveWithTimeAllowed(gameTest);
        // System.out.println("Best Move : " + a);
    }

    public static void testFindJetonsInARow(int[][] grid) {

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
        System.out.println("grid: ");
        printGrid(grid);
        System.out.println("\njetons en ligne: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnLigne[i]);
        }
        System.out.println("\njetons en colonne: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnColonne[i]);
        }
        System.out.println("\njetons en diagNOSEL: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnDiagNOSEL[i]);
        }
        System.out.println("\njetons en diagNOSEC: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnDiagNOSEC[i]);
        }
        System.out.println("\njetons en diagSONEC: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnDiagSONEC[i]);
        }
        System.out.println("\njetons en diagSONEL: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(jetonsEnDiagSONEL[i]);
        }
        System.out.println("\nGrid NOSE: ");
        printGrid(tabDiagNOSE);
        System.out.println("\nGrid SONE: ");
        printGrid(tabDiagSONE);
    }

    static public void printGrid(int[][] grid) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(" " + grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    /*
     * public static void testMinimax(){
     * // depth 3
     * ArrayList<GameInstance> array3 = new ArrayList<GameInstance>();
     * GameInstance g3 = new GameInstance(null,array3,0,3);
     * 
     * // depth 2
     * ArrayList<GameInstance> array2_1 = new ArrayList<GameInstance>();
     * GameInstance g2_1 = new GameInstance(null,array2_1,0,2);
     * 
     * ArrayList<GameInstance> array2_2 = new ArrayList<GameInstance>();
     * GameInstance g2_2 = new GameInstance(null,array2_2,0,2);
     * 
     * array3.add(g2_1);
     * array3.add(g2_2);
     * 
     * // depth 1
     * ArrayList<GameInstance> array1_1 = new ArrayList<GameInstance>();
     * GameInstance g1_1 = new GameInstance(g2_1,array1_1,0,1);
     * 
     * ArrayList<GameInstance> array1_2 = new ArrayList<GameInstance>();
     * GameInstance g1_2 = new GameInstance(g2_1,array1_2,0,1);
     * 
     * array2_1.add(g1_1);
     * array2_1.add(g1_2);
     * 
     * 
     * ArrayList<GameInstance> array1_3 = new ArrayList<GameInstance>();
     * GameInstance g1_3 = new GameInstance(g2_2,array1_3,0,1);
     * 
     * ArrayList<GameInstance> array1_4 = new ArrayList<GameInstance>();
     * GameInstance g1_4 = new GameInstance(g2_2,array1_4,0,1);
     * 
     * array2_2.add(g1_3);
     * array2_2.add(g1_4);
     * 
     * // depth 0
     * ArrayList<GameInstance> array0_1 = new ArrayList<GameInstance>();
     * GameInstance g0_1 = new GameInstance(g1_1,array0_1,5,0);
     * 
     * ArrayList<GameInstance> array0_2 = new ArrayList<GameInstance>();
     * GameInstance g0_2 = new GameInstance(g1_1,array0_2,-4,0);
     * 
     * array1_1.add(g0_1);
     * array1_1.add(g0_2);
     * 
     * ArrayList<GameInstance> array0_3 = new ArrayList<GameInstance>();
     * GameInstance g0_3 = new GameInstance(g1_2,array0_3,3,0);
     * 
     * ArrayList<GameInstance> array0_4 = new ArrayList<GameInstance>();
     * GameInstance g0_4 = new GameInstance(g1_2,array0_4,-6,0);
     * 
     * array1_2.add(g0_3);
     * array1_2.add(g0_4);
     * 
     * ArrayList<GameInstance> array0_5 = new ArrayList<GameInstance>();
     * GameInstance g0_5 = new GameInstance(g1_3,array0_5,1,0);
     * 
     * ArrayList<GameInstance> array0_6 = new ArrayList<GameInstance>();
     * GameInstance g0_6 = new GameInstance(g1_3,array0_6,2,0);
     * 
     * array1_3.add(g0_5);
     * array1_3.add(g0_6);
     * 
     * 
     * ArrayList<GameInstance> array0_7 = new ArrayList<GameInstance>();
     * GameInstance g0_7 = new GameInstance(g1_4,array0_7,-3,0);
     * 
     * ArrayList<GameInstance> array0_8 = new ArrayList<GameInstance>();
     * GameInstance g0_8 = new GameInstance(g1_4,array0_8,-6,0);
     * 
     * array1_4.add(g0_7);
     * array1_4.add(g0_8);
     * 
     * //minimax
     * System.out.println("Best score: " + App.minimax(g3,3,true,-1000,1000)
     * +"\nCalls: "+App.getCalls());
     * 
     * }
     */

}
