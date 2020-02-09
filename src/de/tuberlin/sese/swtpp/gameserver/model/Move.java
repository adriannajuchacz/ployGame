package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;


/**
 * 
 * Represents one move of a player in a certain stage of the game.
 * 
 * May be specialized further to represent game-specific move information.
 *
 */
public class Move implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8030012939073138731L;

	// attributes
	protected String move;
	protected String board;
	public String now;
	public String later;
	public int drehungen;

	// associations
	protected Player player;

	/************************************
	 * constructors
	 ************************************/

	public Move(String move, String boardBefore, Player player) {
		this.move = move;
		this.now = move.substring(0, 2);
		this.later = move.substring(3, 5);
		this.drehungen = Integer.parseInt(move.substring(6));
		this.board = boardBefore;
		this.player = player;
	}

	/************************************
	 * getters/setters
	 ************************************/

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public String getState() {
		return board;
	}

	public void setBoard(String state) {
		this.board = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
