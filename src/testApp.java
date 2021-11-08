import java.util.ArrayList;

public class testApp {
    public static void main(String[] args) throws Exception {
        //testFonctionEval(new GameInstance());
        testMinimax();
    }

    public static void testFonctionEval(GameInstance gameInstance) {
        int[][] grid = new int[][] { { 0, 0, 2, 0, 2, 2, 0, 0 }, { 4, 0, 0, 0, 0, 0, 0, 4 }, { 0, 0, 0, 4, 0, 2, 0, 4 },
                { 4, 0, 0, 0, 2, 0, 0, 4 }, { 4, 2, 0, 0, 0, 0, 0, 4 }, { 4, 0, 2, 4, 0, 0, 0, 0 },
                { 0, 0, 4, 2, 0, 0, 0, 4 }, { 0, 2, 0, 0, 2, 2, 2, 0 } };
        gameInstance.setGrid(grid);
        gameInstance.setJoueurJeton(grid);
        System.out.println(gameInstance.rate() + " = 2");

    }

    public static void testMinimax(){
         // depth 3
         ArrayList<GameInstance> array3 = new ArrayList<GameInstance>();
         GameInstance g3 = new GameInstance(null,array3,0,3);
         
         // depth 2
         ArrayList<GameInstance> array2_1 = new ArrayList<GameInstance>();
         GameInstance g2_1 = new GameInstance(null,array2_1,0,2);

         ArrayList<GameInstance> array2_2 = new ArrayList<GameInstance>();
         GameInstance g2_2 = new GameInstance(null,array2_2,0,2);

         array3.add(g2_1);
         array3.add(g2_2);
         
         // depth 1
         ArrayList<GameInstance> array1_1 = new ArrayList<GameInstance>();
         GameInstance g1_1 = new GameInstance(g2_1,array1_1,0,1);

         ArrayList<GameInstance> array1_2 = new ArrayList<GameInstance>();
         GameInstance g1_2 = new GameInstance(g2_1,array1_2,0,1);

         array2_1.add(g1_1);
         array2_1.add(g1_2);


         ArrayList<GameInstance> array1_3 = new ArrayList<GameInstance>();
         GameInstance g1_3 = new GameInstance(g2_2,array1_3,0,1);

         ArrayList<GameInstance> array1_4 = new ArrayList<GameInstance>();
         GameInstance g1_4 = new GameInstance(g2_2,array1_4,0,1);

         array2_2.add(g1_3);
         array2_2.add(g1_4);

         // depth 0
         ArrayList<GameInstance> array0_1 = new ArrayList<GameInstance>();
         GameInstance g0_1 = new GameInstance(g1_1,array0_1,5,0);

         ArrayList<GameInstance> array0_2 = new ArrayList<GameInstance>();
         GameInstance g0_2 = new GameInstance(g1_1,array0_2,-4,0);

         array1_1.add(g0_1);
         array1_1.add(g0_2);

         ArrayList<GameInstance> array0_3 = new ArrayList<GameInstance>();
         GameInstance g0_3 = new GameInstance(g1_2,array0_3,3,0);

         ArrayList<GameInstance> array0_4 = new ArrayList<GameInstance>();
         GameInstance g0_4 = new GameInstance(g1_2,array0_4,-6,0);
        
         array1_2.add(g0_3);
         array1_2.add(g0_4);

         ArrayList<GameInstance> array0_5 = new ArrayList<GameInstance>();
         GameInstance g0_5 = new GameInstance(g1_3,array0_5,1,0);

         ArrayList<GameInstance> array0_6 = new ArrayList<GameInstance>();
         GameInstance g0_6 = new GameInstance(g1_3,array0_6,2,0);

         array1_3.add(g0_5);
         array1_3.add(g0_6);


         ArrayList<GameInstance> array0_7 = new ArrayList<GameInstance>();
         GameInstance g0_7 = new GameInstance(g1_4,array0_7,-3,0);

         ArrayList<GameInstance> array0_8 = new ArrayList<GameInstance>();
         GameInstance g0_8 = new GameInstance(g1_4,array0_8,-6,0);

         array1_4.add(g0_7);
         array1_4.add(g0_8);

         //minimax
         System.out.println("Best score: " + App.minimax(g3,3,true,-1000,1000) +"\nCalls: "+App.getCalls());

    }

}
