import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class App {

	static private long startTime;
	static private long timeAllowed = 4;
	static private boolean isWhite;

	/**
	 * order of operations :
	 * - ouvrir BoardGame.exe
	 * - choisir quel joueur est l'algorithme en cliquant le tickmark "Réseau" pour
	 * ce joueur
	 * - cliquer sur l'étoile dans le côté droit du menu pour commencer une nouvelle
	 * partie
	 * - runner le main ci-dessous
	 */
	public static void main(String[] args) {

		Socket MyClient;
		BufferedInputStream input;
		BufferedOutputStream output;
		GameInstance currentGameState = new GameInstance();
		int[][] board = new int[8][8];
		try {
			MyClient = new Socket("localhost", 8888);

			input = new BufferedInputStream(MyClient.getInputStream());
			output = new BufferedOutputStream(MyClient.getOutputStream());
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				char cmd = 0;

				cmd = (char) input.read();
				System.out.println(cmd);
				// Debut de la partie en joueur blanc
				if (cmd == '1') {
					byte[] aBuffer = new byte[1024];

					int size = input.available();
					System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board[x][y] = Integer.parseInt(boardValues[i]);
						System.out.printf("%d", board[x][y]);
						x++;
						if (x == 8) {
							x = 0;
							y++;
							System.out.printf("\n");
						}
					}
					currentGameState.setGrid(board);

					System.out.println("Nouvelle partie! Vous jouez blanc, entrez votre premier coup : ");
					String move = null;
					isWhite = true;

					move = getBestMoveWithTimeAllowed(currentGameState, isWhite);
					System.out.println("Move : " + move);

					output.write(move.getBytes(), 0, move.length());
					output.flush();
				}
				// Debut de la partie en joueur Noir
				if (cmd == '2') {
					System.out.println("Nouvelle partie! Vous jouez noir, attendez le coup des blancs");
					byte[] aBuffer = new byte[1024];
					isWhite = false;
					int size = input.available();
					System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board[x][y] = Integer.parseInt(boardValues[i]);
						System.out.printf("%d", board[x][y]);
						x++;
						if (x == 8) {
							x = 0;
							y++;
							System.out.printf("\n");
						}
					}
					currentGameState.setGrid(board);
				}

				// Le serveur demande le prochain coup
				// Le message contient aussi le dernier coup joue.
				if (cmd == '3') {
					byte[] aBuffer = new byte[16];

					int size = input.available();
					System.out.println("size :" + size);
					input.read(aBuffer, 0, size);

					String s = new String(aBuffer);
					System.out.println("Dernier coup :" + s);
					System.out.println("Entrez votre coup : ");
					String move = null;
					move = getBestMoveWithTimeAllowed(currentGameState, isWhite);
					output.write(move.getBytes(), 0, move.length());
					output.flush();
				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.exit(0);
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					move = getBestMoveWithTimeAllowed(currentGameState, isWhite);
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
				if (cmd == '5') {
					System.out.println("Partie terminé");

					byte[] aBuffer = new byte[16];

					int size = input.available();
					input.read(aBuffer, 0, size);

					String s = new String(aBuffer);
					System.out.println("Dernier coup :" + s);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getBestMoveWithTimeAllowed(GameInstance gameInstance, boolean isMaxPlayer) {
		startTime = System.nanoTime();

		int score = minimax(gameInstance, 1, isMaxPlayer, -100000000, 100000000);
		return gameInstance.getNextMove(score);
	}

	// Minimax + alpha-beta pruning
	public static int minimax(GameInstance gameInstance, int dept, boolean isMaxPlayer, int alpha, int beta) {

		if (dept <= 0 || gameInstance.gameIsOver()) {
			gameInstance.rate();
			return gameInstance.getScore();
		}
		gameInstance.generateChildren();
		System.out.println(gameInstance.getChildren().size());
		if (isMaxPlayer) {
			int maxRating = -1000000000;
			for (int i = 0; i < gameInstance.getChildren().size(); i++) {
				GameInstance child = gameInstance.getChildren().get(i);
				int eval = minimax(child, dept - 1, false, alpha, beta);
				maxRating = Math.max(maxRating, eval);
				gameInstance.setScore(maxRating);
				alpha = Math.max(eval, alpha);
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
				int eval = minimax(child, dept - 1, true, alpha, beta);
				minRating = Math.min(minRating, eval);
				gameInstance.setScore(minRating);
				beta = Math.min(eval, beta);
				if (beta <= alpha) {
					System.out.println("PRUNED!");
					break;
				}
			}
			return minRating;
		}
	}
}
