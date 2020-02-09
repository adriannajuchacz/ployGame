package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;

public class Board implements Serializable{
	

	private static final long serialVersionUID = -1334241242091094325L;
	
	public String [][] board;
	private final static String Zahlen = "abcdefghij";
	private final static String echtZahlen = "987654321";
	
	public Board(String s){
		board = new String[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (j == 8) {
					if (i == 8) board[j][i] = s;
					else {
						board[j][i] = s.substring(0, s.indexOf("/"));
						s = s.substring(s.indexOf("/") + 1);
					}
				} else {
					board[j][i] = s.substring(0, s.indexOf(","));
					s = s.substring(s.indexOf(",") + 1);
				}
			}
		}
	}
	
	public void setPos(String s, String pos) {
		this.board[Zahlen.indexOf(pos.charAt(0))][echtZahlen.indexOf(pos.charAt(1))] = s;
	}
	
	public String BoardAt(String s) {
		return this.board[Zahlen.indexOf(s.charAt(0))][echtZahlen.indexOf(s.charAt(1))];
	}
	
	public String BoardAt(int x, int y) {
		return this.board[x][y];
	}
	
	public String BoardtoString() {
		String s = "";
		for (int j = 0; j < 9; j++) {
			for (int i = 0; i < 9; i++) {
				if (i != 8) s += this.board[i][j] + ",";
				else s += this.board[i][j];
			}
			if (j != 8) s += "/";
		}
		return s;
	}
	
	public boolean opponentHasCommander(String currentPlayer) {
		String diagonalCommander, normalCommander;
		if(currentPlayer == "w") {
			diagonalCommander = "b170";
			normalCommander = "b85";
		} else {
			diagonalCommander = "w170";
			normalCommander = "w85";
		}
		
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) { 
				if (board[row][col].equals(diagonalCommander) || board[row][col].equals(normalCommander)) {
					return true;
				}
			} 
		}
		return false;
	}
	
	public boolean opponentHasOneStein(String currentPlayer) {
		int numberFigures = 0;
		String opponent;
		if(currentPlayer == "w") {
			opponent = "b";
		} else {
			opponent = "w";
		}
		
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) { 
				if (board[row][col].startsWith(opponent)) {
					numberFigures++;
				}
			} 
		} 
		return (numberFigures == 1);
	}
}
