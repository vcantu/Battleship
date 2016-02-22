package battleship;

public class Position {
	int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Position p) {
		return (x == p.x && y == p.y);
	}
	
	public Position getAdjacent(Orientation o) {
		switch(o) {
			case EAST:
				return new Position(x+1, y);
			case NORTH:
				return new Position(x, y-1);
			case SOUTH:
				return new Position(x, y+1);
			case WEST:
				return new Position(x-1, y);
			default:
				return this;
		}
	}
}
