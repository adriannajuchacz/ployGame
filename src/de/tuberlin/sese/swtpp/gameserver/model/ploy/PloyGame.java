package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.Board;
import de.tuberlin.sese.swtpp.gameserver.model.Stein;
import de.tuberlin.sese.swtpp.gameserver.model.Move;

/**
 * Class Cannon extends the abstract class Game as a concrete game instance that
 * allows to play Cannon.
 *
 */
public class PloyGame extends Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here

	private Board board;
	private boolean GameOver;
	String Zahlen = "abcdefghi";
	String echtZahlen = "987654321";

	/************************
	 * constructors
	 ***********************/

	public PloyGame() {
		super();
		// TODO: init internal representation
		String currentBoard = ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,";
		this.GameOver = false;
		this.board = new Board(currentBoard);
	}

	public String getType() {
		return "ploy";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.blackPlayer= players.get(0);
				this.whitePlayer = players.get(1);
				nextPlayer = blackPlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}
	
	// nächster Spieler
	public Player nextPlayer() {
		if (nextPlayer == whitePlayer) return blackPlayer;
		else return whitePlayer;
	}
	
	// drehen
	public String angle(String stein) {
		int pos = Integer.parseInt(stein.substring(1));
		System.out.println("davor:" + pos);
		if (pos > 127) {
			pos -= 128;
			pos *= 2;
			pos++;
		}
		else pos = pos * 2;
		System.out.println(pos);

	    return stein.charAt(0) + Integer.toString(pos);
	}
	
	//richtiges Format
	public boolean format(String s) {
		if (s.length() != 2) return false;
		if (!Zahlen.contains(s.substring(0,1))) return false;
		if (!echtZahlen.contains(s.substring(1))) return false;
		return true;
	}
	
	public boolean GameOver() {
		String currentPlayer;
		if (nextPlayerString() == "w") {
			currentPlayer = "b";
		} else {
			currentPlayer = "w";
		}
		boolean opponentHasCommander = this.board.opponentHasCommander(currentPlayer);
		boolean opponentHasOneStein = this.board.opponentHasOneStein(currentPlayer);
		if(opponentHasOneStein && opponentHasCommander) {
			GameOver = true;
			return true;
		} else if(!opponentHasCommander) {
			GameOver = true;
			return true;
		}
		return false;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(p -> p.requestedDraw())) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/*******************************************
	 * Helpful stuff
	 ******************************************/

	/**
	 * 
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 * 
	 * @param player
	 * @return
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}
	

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {
		// implemented	
		board = new Board(state);
		// Note: This method is for automatic testing. A regular game would not start at some artificial state. 
		//       It can be assumed that the state supplied is a regular board that can be reached during a game. 
	}

	@Override
	public String getBoard() {
		// implemented and replace dummy with actual board
		return board.BoardtoString();
	}
	
	public boolean possible(String move, Player player) {
		if (player != nextPlayer) return false;
		if (move.length() != 7) return false;
		Move m = new Move(move, board.BoardtoString(), player);
		if (!format(m.now) || !format(m.later) || !(m.drehungen < 8)) return false;
		Stein me = new Stein(m.now, board);
		if (me.getType() > 1 && !m.now.equals(m.later) && m.drehungen > 0) return false;
		if (!me.partofmögliche(m.later) && !m.now.equals(m.later)) return false;
		if (GameOver) return false;
		
		// drag&drop but the same position
		if (m.drehungen == 0 && m.now.equals(m.later)) return false;
		
		// player is trying to move opponent's figure
		if ((player == whitePlayer) && (me.player == 'b'))return false;
		if ((player == blackPlayer) && (me.player == 'w'))return false;
		return true;
	}

	@Override
	public boolean tryMove(String moveString, Player player) {
		
		//testen ob moveString möglich ist
		System.out.println(moveString);
		if (!possible(moveString, player)) return false;
		Move m = new Move(moveString, board.BoardtoString(), player);

		/*//wenn man auf das Feld eines gegners kommt
		if (!(board.BoardAt(m.later) == null) && !m.now.equals(m.later)) {
			this.nextPlayer.anzahlSteine--;
			Stein gegner = new Stein(m.later, board);
			if (gegner.Controller || this.nextPlayer.anzahlSteine < 2) GameOver = true;
		}*/
		
		//Stein verschieben
		if (!m.now.equals(m.later)) {
			board.setPos(board.BoardAt(m.now), m.later);
			board.setPos("",m.now);
		}
		
		//Stein drehen
		for (int i = 0; i < m.drehungen; i++){
			board.setPos(angle(board.BoardAt(m.later)), m.later);
		}
		
		//aktualisieren
		history.add(m);
		nextPlayer = nextPlayer();
		
		//testen ob Spiel vorbei
		if (GameOver()) regularGameEnd(player);
		
		return true;
	}
}
