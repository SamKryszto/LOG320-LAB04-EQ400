import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }

    // Minimax + alpha-beta pruning
    int minimax(GameInstance gameInstance, int depth, boolean isMaxPlayer, int alpha, int beta){
        if (depth == 0 || gameInstance.gameIsOver()){
            return rate(gameInstance);
        }
        if (isMaxPlayer){
            int maxRating = -1000000000;
            for(int i = 0; i < gameInstance.getChildren().size(); i++){
                GameInstance child = gameInstance.getChildren().get(i);
                int rating = minimax(child, depth - 1, false, alpha, beta);
                maxRating = Math.max(maxRating, rating);
                alpha = Math.max(rating, alpha);
                if (beta <= alpha){
                    break;
                }
            }
            return maxRating;
        }
        else {
            int minRating = 1000000000;
            for(int i = 0; i < gameInstance.getChildren().size(); i++){
                GameInstance child = gameInstance.getChildren().get(i);
                int rating = minimax(child, depth - 1, true, alpha, beta);
                minRating = Math.min(minRating,rating);
                beta = Math.min(rating, beta);
                if (beta <= alpha){
                    break;
                }
            }
            return minRating;
        }
    }
    // TO DO
    // Determine un score pour la situation actuelle d'un échiquier
    public int rate(GameInstance gameinstance){
        int rate = 0;
        return rate;
    }
}
