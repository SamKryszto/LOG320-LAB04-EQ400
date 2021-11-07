
public class testApp {
    public static void main(String[] args) throws Exception {
        testFonctionEval(new GameInstance());
    }

    public static void testFonctionEval(GameInstance gameInstance) {
        int[][] grid = new int[][] { { 0, 0, 2, 0, 2, 2, 0, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 0, 0, 4, 0, 2, 0, 4 },
                { 4, 0, 0, 0, 2, 0, 0, 4 }, { 4, 2, 0, 0, 0, 0, 0, 4 }, { 4, 0, 2, 4, 0, 0, 0, 0 },
                { 0, 0, 4, 2, 0, 0, 0, 4 }, { 0, 2, 0, 0, 2, 2, 2, 0 } };
        gameInstance.setGrid(grid);
        gameInstance.setJoueurJeton(grid);
        System.out.println(gameInstance.rate() + " = 2");

    }

}
