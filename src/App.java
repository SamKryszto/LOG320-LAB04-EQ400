
public class App {

    //temp

    private static int minimaxCalls;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }

    // Minimax + alpha-beta pruning
    public static int minimax(GameInstance gameInstance, int depth, boolean isMaxPlayer, int alpha, int beta) {
        minimaxCalls++;
        if (depth == 0 || gameInstance.gameIsOver()) {
            //return gameInstance.rate();
            return gameInstance.getScore();
        }
        if (isMaxPlayer) {
            int maxRating = -1000000000;
            for (int i = 0; i < gameInstance.getChildren().size(); i++) {
                GameInstance child = gameInstance.getChildren().get(i);
                int rating = minimax(child, depth - 1, false, alpha, beta);
                maxRating = Math.max(maxRating, rating);
                alpha = Math.max(rating, alpha);
                if (beta <= alpha) {
                    System.out.println("PRUNED!");
                    break;
                }
            }
            return maxRating;
        } else {
            int minRating = 1000000000;
            for (int i = 0; i < gameInstance.getChildren().size(); i++) {
                GameInstance child = gameInstance.getChildren().get(i);
                int rating = minimax(child, depth - 1, true, alpha, beta);
                minRating = Math.min(minRating, rating);
                beta = Math.min(rating, beta);
                if (beta <= alpha) {
                    System.out.println("PRUNED!");
                    break;
                }
            }
            return minRating;
        }
    }
    //temp
    public static int getCalls(){
        return minimaxCalls;
    }
}
