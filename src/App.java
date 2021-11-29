import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class App {

	static private double startTime;
	static private double timeAllowed = 2 * Math.pow(10, 9);
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

		GameInstance currentGameState = new GameInstance();
		GameInstance newInst;
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

					long bitBoardBlancs = 0L;
					long bitBoardNoirs = 0L;

					for (int i = 0; i < s.length(); i++) {
						char c = s.charAt(i);
						if (c == '2') {
							bitBoardNoirs = bitBoardNoirs * 2 + 1;
							bitBoardBlancs = bitBoardBlancs * 2;
						}
						else if (c == '4'){
							bitBoardNoirs = bitBoardNoirs * 2;
							bitBoardBlancs = bitBoardBlancs * 2 + 1;
						}
						else if (c == ' '){}
						else {
							bitBoardNoirs = bitBoardNoirs * 2;
							bitBoardBlancs = bitBoardBlancs * 2;
						}
						
					}
					System.out.println(Long.toBinaryString(bitBoardBlancs));
					System.out.println(Long.toBinaryString(bitBoardNoirs));

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

					move = app.getBestMoveWithTimeAllowed(currentGameState, isWhite);
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
					for (int i = 0; i < board.length; i++) {
						for (int j = 0; j < board[i].length; j++) {
							System.out.print(board[i][j]);
						}
						System.out.println("");
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
					newInst = new GameInstance(board, isWhite, a);

					for (int i = 0; i < board.length; i++) {
						for (int j = 0; j < board[i].length; j++) {
							System.out.print(board[i][j]);
						}
						System.out.println("");
					}
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
					for (int i = 0; i < board.length; i++) {
						for (int j = 0; j < board[i].length; j++) {
							System.out.print(board[i][j]);
						}
						System.out.println("");
					}
					newInst = new GameInstance(board, !isWhite, move);

					output.flush();
				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.exit(0);
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					move = app.getBestMoveWithTimeAllowed(currentGameState, isWhite);
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

		startTime = System.nanoTime();
		int score = minimax(gameInstance, isMaxPlayer, -100000000, 100000000);
		return gameInstance.getNextMove(score);
	}

	// Minimax + alpha-beta pruning
	public int minimax(GameInstance gameInstance, boolean isMaxPlayer, int alpha, int beta) {

		boolean timesUp = ((System.nanoTime() - startTime) > timeAllowed);

		if (timesUp || gameInstance.gameIsOver()) {
			if (!gameInstance.gameIsOver()) {
				gameInstance.rate();
			}
			return gameInstance.getScore();

		}
		gameInstance.generateChildren();
		ArrayList<GameInstance> children = gameInstance.getChildren();
		System.out.println(children.size());
		if (isMaxPlayer) {
			int maxRating = -1000000000;
			for (int i = 0; i < children.size(); i++) {
				GameInstance child = children.get(i);
				int eval = minimax(child, false, alpha, beta);
				maxRating = Math.max(maxRating, eval);
				gameInstance.setScore(maxRating);
				alpha = Math.max(eval, alpha);
				if (beta <= alpha) {
					break;
				}
			}
			return maxRating;

		} else {
			int minRating = 1000000000;
			for (int i = 0; i < children.size(); i++) {
				GameInstance child = children.get(i);
				int eval = minimax(child, true, alpha, beta);
				minRating = Math.min(minRating, eval);
				gameInstance.setScore(minRating);
				beta = Math.min(eval, beta);
				if (beta <= alpha) {
					break;
				}
			}
			return minRating;
		}
	}
}
