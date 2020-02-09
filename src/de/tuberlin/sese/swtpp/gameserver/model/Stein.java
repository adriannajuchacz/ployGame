package de.tuberlin.sese.swtpp.gameserver.model;

import java.util.LinkedList;

import de.tuberlin.sese.swtpp.gameserver.model.Board;
import de.tuberlin.sese.swtpp.gameserver.model.Position;


public class Stein {
	
	int typ;
	public char player;
	LinkedList<String> mögliche;
	LinkedList<Integer> dir;
	Position pos;
	public boolean Controller;
	private String state;
	public Board board;
	private final static int[] xd = {0, 1, 1, 1, 0, -1, -1, -1};
    private final static int[] yd = {-1, -1, 0, 1, 1, 1, 0, -1};
	
	public Stein (String s, Board b){
		if (b.BoardAt(s).length() < 2) System.out.println("Feld ist leeeeeer");
		state = b.BoardAt(s).substring(1); 
		String bitstate = (Integer.toBinaryString(Integer.parseInt(state)));
		typ = bits(bitstate);
		System.out.println(bitstate);
		player = b.BoardAt(s).charAt(0);
		dir = new LinkedList<>();
		directions(bitstate);
		mögliche = new LinkedList<>();
		pos = new Position(s);
		board = b;
		möglich(state);
		if (dir.size() > 3) typ = 1;
	}
	
	//anzahl gesetzter bits
	public int bits(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '1') count++;
		return count;
	}
	
	//mögliche richtungen
	public void directions(String s) {
		for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '1') dir.add(s.length() - 1 - i);
	}
	
	//Position nicht außerhalb des felds
	public boolean inRange(int x) {
		if (x < 0 || x > 8) return false;
		return true;
	}
	
	public int getType() {
		return typ;
	}
	
	//ist die Ziel position möglich?
	public boolean partofmögliche(String s){
		if (mögliche.contains(s)) return true;
		return false;
	}
	
	public void möglich(String s) {
		for (int d : dir) {	
			Position p = new Position(pos);
			for (int i = 0; i < typ; i++) {
				p.displace(xd[d], yd[d]);
				if (inRange(p.x) && inRange(p.y)) {
					if (board.BoardAt(p.x, p.y).length() == 0) mögliche.add(p.PostoString());
					else {
						if (!(board.BoardAt(p.x, p.y).charAt(0) == player)) {
							mögliche.add(p.PostoString());
							break;
						}	
					}
				} 
			}
		}
	}
}