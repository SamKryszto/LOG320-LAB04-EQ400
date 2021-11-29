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
	private int depth = 4;

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
		int xy1;
		int xy2;
		long bitBoardAllie = 0L;
		long bitBoardAdverse = 0L;
		long[] newBitBoards;

		GameInstance currentGameState = new GameInstance();
		GameInstance newInst = new GameInstance();
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

					// for (int i = 0; i < s.length(); i+=2) {
					// 	char c = s.charAt(i);
					// 	if (c == '2') {
					// 		bitBoardAdverse = bitBoardAdverse * 2 + 1;
					// 		bitBoardAllie = bitBoardAllie * 2;
					// 	}
					// 	else if (c == '4'){
					// 		bitBoardAdverse = bitBoardAdverse * 2;
					// 		bitBoardAllie = bitBoardAllie * 2 + 1;
					// 	}
					// 	else {
					// 		bitBoardAdverse = bitBoardAdverse * 2;
					// 		bitBoardAllie = bitBoardAllie * 2;
					// 	}
						
					// }
					// System.out.println(Long.toBinaryString(bitBoardBlancs));
					// System.out.println(Long.toBinaryString(bitBoardNoirs));

					System.out.println("Nouvelle partie! Vous jouez blanc, entrez votre premier coup : ");
					String move = null;
					isWhite = true;

					newInst = app.getBestMoveWithTimeAllowed(currentGameState, isWhite);
					move = newInst.getLastMoveString();
					r1 = move.charAt(1);
					c1 = move.charAt(0);
					r2 = move.charAt(3);
					c2 = move.charAt(2);
					System.out.println("Move : " + newInst.getLastMoveString());

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
					for (int i = 0; i < s.length(); i+=2) {
						char c = s.charAt(i);
						if (c == '2') {
							bitBoardAdverse = bitBoardAdverse * 2;
							bitBoardAllie = bitBoardAllie * 2 + 1;
						}
						else if (c == '4'){
							bitBoardAdverse = bitBoardAdverse * 2 + 1;
							bitBoardAllie = bitBoardAllie * 2;
						}
						else {
							bitBoardAdverse = bitBoardAdverse * 2;
							bitBoardAllie = bitBoardAllie * 2;
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

					x1 = (Character.getNumericValue(r1) - 1) * 8;
					y1 = 8 - ((int)(c1 - 65) + 1);
					x2 = (Character.getNumericValue(r2) - 1) * 8;
					y2 = 8 - ((int)(c2 - 65) + 1);
					xy1 = x1 + y1;
					xy2 = x2 + y2;

					System.out.println("DernierC : " + Masks.getMovementCode(xy1, xy2));
					
					newBitBoards = Masks.movePiece(xy1, xy2, bitBoardAdverse, bitBoardAllie);

					bitBoardAdverse = newBitBoards[0];
					bitBoardAllie = newBitBoards[1];

					// ArrayList<GameInstance> children = newInst.getChildren();
					// boolean found;
					// if (isWhite) {
					// 	for (int i = 0; i < children.size(); i++) {
					// 		found = children.get(i).compareGrids(bitBoardAllie,bitBoardAdverse);
					// 		if (found) {
					// 			newInst = children.get(i);

					// 		} else {
					// 			newInst = new GameInstance(bitBoardAllie, bitBoardAdverse, !isWhite, Masks.getMovementCode(xy1, xy2));
					// 		}
					// 	}
					// }
					// else {
					// 	for (int i = 0; i < children.size(); i++) {
					// 		found = children.get(i).compareGrids(bitBoardAdverse, bitBoardAllie);
					// 		if (found) {
					// 			newInst = children.get(i);
	
					// 		} else {
					// 			newInst = new GameInstance(bitBoardAdverse, bitBoardAllie, !isWhite, Masks.getMovementCode(xy1, xy2));
					// 		}
					// 	}
					// }
					

					if (isWhite) {
						newInst = new GameInstance(bitBoardAllie, bitBoardAdverse, isWhite, Masks.getMovementCode(xy1, xy2));
					}
					else {
						newInst = new GameInstance(bitBoardAdverse, bitBoardAllie, isWhite, Masks.getMovementCode(xy1, xy2));
					}

					System.out.println("Entrez votre coup : ");
					String move = null;
					newInst = app.getBestMoveWithTimeAllowed(newInst, isWhite);
					move = newInst.getLastMoveString();
					output.write(move.getBytes(), 0, move.length());

					System.out.println("Notre Move : " + move);
					
					output.flush();
				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.exit(0);
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					newInst = app.getBestMoveWithTimeAllowed(currentGameState, isWhite);
					move = newInst.getLastMoveString();
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

	public GameInstance getBestMoveWithTimeAllowed(GameInstance gameInstance, boolean isMaxPlayer) {

		timeStart = System.currentTimeMillis();

		int score = minimax(gameInstance, isMaxPlayer, depth, -100000000, 100000000);

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

			for (int i = 0; i < children.size(); i++) {
				GameInstance child = children.get(i);
				int eval = minimax(child, !isMaxPlayer, depth - 1, alpha, beta);
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
				int eval = minimax(child, !isMaxPlayer, depth - 1, alpha, beta);
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
