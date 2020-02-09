package de.tuberlin.sese.swtpp.gameserver.model;

public class Position {
	public int x;
    public int y;
    private final static String Zahlen = "abcdefghi";
    private final static String echtZahlen = "987654321";

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Position(Position that) {
        this(that.x, that.y);
    }

    public Position (String s) {
    	this.x = Zahlen.indexOf(s.charAt(0));
    	this.y = echtZahlen.indexOf(s.charAt(1));
    }
    
    public void displace(int xd, int yd) {
        this.x += xd;
        this.y += yd;
    }
    
    public String PostoString() {
    	return Character.toString(Zahlen.charAt(x)) + Character.toString(echtZahlen.charAt(y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position that = (Position) o;
        return x == that.x && y == that.y;
    }
}
