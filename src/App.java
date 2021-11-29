import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;

public class App {

	static private boolean isWhite;
	private long timeStart;
	private long timeDelay = 4950;
	private int countDepth;
	private int depth = 5;

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
		App app = new App();
		Socket MyClient;
		BufferedInputStream input;
		BufferedOutputStream output;
		char r1;
		char c1;
		char r2;
		char c2;
		int x1;
		int y1;
		int x2;
		int y2;

		GameInstance newInst = new GameInstance();
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
						y++;
						if (y == 8) {
							y = 0;
							x++;
							System.out.printf("\n");
						}
					}

					System.out.println("Nouvelle partie! Vous jouez blanc, entrez votre premier coup : ");
					String move = null;
					isWhite = true;

					move = app.getBestMoveWithTimeAllowed(newInst, isWhite);
					r1 = move.charAt(2);
					c1 = move.charAt(1);
					r2 = move.charAt(4);
					c2 = move.charAt(3);
					System.out.println("Move : " + r1 + "/" + c1 + "," + r2 + "/" + c2);
					x1 = 8 - Character.getNumericValue(r1);
					y1 = (char) (c1 - 65);
					x2 = 8 - Character.getNumericValue(r2);
					y2 = (char) (c2 - 65);

					board[x1][y1] = 0;
					if (!isWhite) {
						board[x2][y2] = 2;
					} else {
						board[x2][y2] = 4;
					}

					newInst = new GameInstance(board, isWhite, move);

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
						y++;
						if (y == 8) {
							y = 0;
							x++;
							System.out.printf("\n");
						}
					}
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
					r1 = s.charAt(2);
					c1 = s.charAt(1);
					r2 = s.charAt(7);
					c2 = s.charAt(6);

					x1 = 8 - Character.getNumericValue(r1);
					y1 = (char) (c1 - 65);
					x2 = 8 - Character.getNumericValue(r2);
					y2 = (char) (c2 - 65);
					System.out.println("DernierC :" + x1 + "" + y1 + "," + x2 + "" + y2);
					board[x1][y1] = 0;
					if (isWhite) {
						board[x2][y2] = 2;
					} else {
						board[x2][y2] = 4;
					}
					String a = c1 + "" + r1 + "" + c2 + "" + r2;
					
					// ArrayList<GameInstance> children = newInst.getChildren();
					// boolean found = false;
					// for (int i = 0; i < children.size(); i++) {
					// 	found = children.get(i).compareGrids(board);
					// 	if (found) {
					// 		newInst = children.get(i);
					// 		break;

					// 	} 
					// }
					// if (!found) {
						newInst = new GameInstance(board, isWhite, a);
					// }
					System.out.println("Entrez votre coup : ");
					String move = null;
					move = app.getBestMoveWithTimeAllowed(newInst, isWhite);
					output.write(move.getBytes(), 0, move.length());

					r1 = move.charAt(2);
					c1 = move.charAt(1);
					r2 = move.charAt(4);
					c2 = move.charAt(3);
					System.out.println("Move : " + r1 + "/" + c1 + "," + r2 + "/" + c2);
					x1 = 8 - Character.getNumericValue(r1);
					y1 = (char) (c1 - 65);
					x2 = 8 - Character.getNumericValue(r2);
					y2 = (char) (c2 - 65);

					System.out.println("NotreMove : " + move + " / " + x1 + "" + y1 + "," + x2 + "" + y2);
					board[x1][y1] = 0;
					if (!isWhite) {
						board[x2][y2] = 2;
					} else {
						board[x2][y2] = 4;
					}
					//
					output.flush();
				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.exit(0);
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					move = app.getBestMoveWithTimeAllowed(newInst, isWhite);
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

	public String getBestMoveWithTimeAllowed(GameInstance gameInstance, boolean isMaxPlayer) {

		timeStart = System.currentTimeMillis();
		int score = 0;
		int i = 0;

		while (i < depth && !gameInstance.gameIsOver()){
			System.out.println(("depth : " + i));
			score = minimax(gameInstance, isMaxPlayer, i, -100000000, 100000000);
			System.out.println("score : " + score);
			if (gameInstance.getTurn() && score == 1000000) {
				return gameInstance.getNextMove(score);
			}
			else if (!gameInstance.getTurn() && score == -1000000) {
				return gameInstance.getNextMove(score);
			}
			i++;
		}
		return gameInstance.getNextMove(score);
	}

	// Minimax + alpha-beta pruning
	public int minimax(GameInstance gameInstance, boolean isMaxPlayer, int depth, int alpha,
			int beta) {

		if (System.currentTimeMillis() - timeStart >= timeDelay || gameInstance.gameIsOver() || depth <= 0) {
			if (!gameInstance.gameIsOver()) {
				gameInstance.rate();
			}
			return gameInstance.getScore();
		}

		ArrayList<GameInstance> children = gameInstance.getChildren();

		
		if (children.size() <= 0) {
		gameInstance.generateChildren();
		}
		if (isMaxPlayer) {
			int maxRating = -1000000000;
			//for (int i = 1; i < depth; i++) {
				for (int j = 0; j < children.size(); j++) {
				
					GameInstance child = children.get(j);
					int eval = minimax(child, !isMaxPlayer, depth - 1, alpha, beta);
					maxRating = Math.max(maxRating, eval);
					if (gameInstance.getScore() != 1000000){
						gameInstance.setScore(maxRating);
					}
					alpha = Math.max(eval, alpha);
					if (beta <= alpha) {
						break;
					}
				}
			//}
			return maxRating;
		
		} else {
			int minRating = 1000000000;
			//for (int i = 1; i < depth; i++) {
				for (int j = 0; j < children.size(); j++) {
				
					GameInstance child = children.get(j);
					int eval = minimax(child, !isMaxPlayer, depth - 1, alpha, beta);
					minRating = Math.min(minRating, eval);
					if (gameInstance.getScore() != -1000000){
						gameInstance.setScore(minRating);
					}
					beta = Math.min(eval, beta);
					if (beta <= alpha) {
						break;
					}
				}
			//}
			return minRating;
		}
		
	}
}
