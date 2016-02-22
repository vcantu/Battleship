package battleship;

import java.awt.Point;

import gameEngine.Grid;

public class BattleshipGrid extends Grid {
	
	boolean[][] occupied;
	PositionStatus[][] posStatus = new PositionStatus[10][10];
	Battleship[] ships;
	
	public BattleshipGrid(Point point, int w, int h) {
		super(point, w, h);
		occupied = new boolean[WIDTH][HEIGHT];
		for (int x=0; x<posStatus.length; x++) {
			for (int y=0; y<posStatus[x].length; y++) {
				posStatus[x][y] = PositionStatus.UNKNOWN;
			}
		}
	}

	public void update(Battleship[] Ships) {
		ships = Ships;
		//sets all to false
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				occupied[x][y] = false;
			}
		}
		//set any occupied to true
		for (Battleship b : ships) {
			if (b != null) {
				for (Position p : b.positions()) {
					if (p != null && isValid(p.x, p.y) && b.onGrid()) {
						occupied[p.x][p.y] = true;
					}
				}
			}
		}
	}
	
	public void postUpdate(Battleship[] Ships) {
		for (Battleship b : Ships) {
			boolean sunk = true;
			for (Position p : b.positions()) {
				if (posStatus[p.x][p.y] != PositionStatus.HIT)
					sunk = false;
			}
			if (sunk) {
				for (Position p : b.positions()) {
					posStatus[p.x][p.y] = PositionStatus.SUNK;
				}
			}
		}
	}
	
	public void shoot(int x, int y) {
		if (!isValid(x,y))
			return;
		if (occupied[x][y]) {
			posStatus[x][y] = PositionStatus.HIT;
		} else {
			posStatus[x][y] = PositionStatus.MISS;
		}
	}
	
	public Battleship getShip(int x, int y) {
		for (Battleship b : ships) {
			for (Position p : b.positions()) {
				if (p.x == x && p.y == y)
					return b;
			}
		}
		return null;
	}
	
	public PositionStatus statusOf(int x, int y) {
		return posStatus[x][y];
	}
	
	public boolean isValid(int x, int y) {
		return !(x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1);
	}
	public boolean isEmpty(int x, int y) {
		return !occupied[x][y];
	}
	public void setSpace(int x, int y, boolean b) { occupied[x][y] = false; }
	
	public Battleship[] ships() { return ships; }
}