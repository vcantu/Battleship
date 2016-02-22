package battleship;

import java.util.ArrayList;

public abstract class Enemy {
	
	public Enemy() {}
	public abstract Battleship[] placeShips(BattleshipGrid grid);
	public abstract void shoot(BattleshipGrid grid);	
	
	//helpers
	private static int getSize(int i) {
		i = 5 - i;
		if(i<3)
			return i+1;
		return i;
	}
	
	private static boolean valid(Position p, int size, Orientation orientation, BattleshipGrid grid) {
		boolean b = true;
		Position[] positions = new Position[size];
		positions[0] = p;
		switch (orientation) {
			case EAST:	
				for (int i=1; i<size; i++) {
					positions[i] = new Position(positions[i-1].x + 1, positions[i-1].y);
				}
				break;
			case NORTH:
				for (int i=1; i<size; i++) {
					positions[i] = new Position(positions[i-1].x, positions[i-1].y - 1);
				}
				break;
			case SOUTH:
				for (int i=1; i<size; i++) {
					positions[i] = new Position(positions[i-1].x, positions[i-1].y + 1);
				}
				break;
			case WEST:
				for (int i=1; i<size; i++) {
					positions[i] = new Position(positions[i-1].x - 1, positions[i-1].y);
				}
				break;
		}
		for (Position pos : positions) {
			if (!grid.isValid(pos.x, pos.y)) {
				b = false;
				break;
			} else {
				if (!grid.isEmpty(pos.x, pos.y)) {
					b = false;
					break;
				}
			}
		}
		return b;
	}

	private static boolean shouldPlace(Position p, int size, Orientation orientation, BattleshipGrid grid) {
		boolean b = true;
		Position[] positions = new Position[(size * 2) + 2];
		switch (orientation) {
			case EAST:
				positions[0] = new Position(p.x-1, p.y);
				positions[1] = new Position(p.x+size, p.y);
				for (int i=0; i<size; i++) {
					positions[2+(2*i)] = new Position(p.x + i, p.y + 1);
					positions[2+(2*i)+1] = new Position(p.x + i, p.y - 1);
				}
				break;
			case NORTH:
				positions[0] = new Position(p.x, p.y+1);
				positions[1] = new Position(p.x, p.y-size);
				for (int i=0; i<size; i++) {
					positions[2+(2*i)] = new Position(p.x + 1, p.y + i);
					positions[2+(2*i)+1] = new Position(p.x - 1, p.y + i);
				}
				break;
			case SOUTH:
				positions[0] = new Position(p.x, p.y-1);
				positions[1] = new Position(p.x, p.y+size);
				for (int i=0; i<size; i++) {
					positions[2+(2*i)] = new Position(p.x + 1, p.y - i);
					positions[2+(2*i)+1] = new Position(p.x - 1, p.y - i);
				}
				break;
			case WEST:
				positions[0] = new Position(p.x+1, p.y);
				positions[1] = new Position(p.x-size, p.y);
				for (int i=0; i<size; i++) {
					positions[2+(2*i)] = new Position(p.x - i, p.y + 1);
					positions[2+(2*i)+1] = new Position(p.x - i, p.y - 1);
				}
				break;
		}
		for (Position pos : positions) {
			if (!grid.isValid(pos.x, pos.y)) {
				b = false;
				break;
			} else {
				if (!grid.isEmpty(pos.x, pos.y)) {
					b = false;
					break;
				}
			}
		}
		return b;
	}
	private static boolean same(ArrayList<Position> P, BattleshipGrid grid) {
		for (int i=0; i<P.size(); i++) {
			ShipType type = grid.getShip(P.get(i).x, P.get(i).y).type();
			for (int j=0; j<P.size(); j++) {
				if (type != grid.getShip(P.get(j).x, P.get(j).y).type())
					return false;
			}
		}
		return true;
	}
	
	private static boolean isEnd(Position p, ArrayList<Position> P) {
		int c = 0;
		for (int o=0; o<4; o++) {
			for (Position pos : P) {
				if (p.getAdjacent(Orientation.values()[o]).equals(pos)) {
					c++;
				}
			}
		}
		if (c > 1)
			return false;
		if (c == 0)
			return true;
		return true;
	}
	
	private static Orientation getEnd(Position p, ArrayList<Position> P) {
		for (int o=0; o<4; o++) {
			for (Position pos : P) {
				if (p.getAdjacent(Orientation.values()[o]).equals(pos)) {
					o += 2;
					if (o==5)
						o=1;
					else if (o==4)
						o=0;
					return Orientation.values()[o];
				}
			}
		}
		return null;
	}
	
	private static ArrayList<Position> getHits(ArrayList<Position> P, BattleshipGrid grid) {
		ArrayList<Position> pos = new ArrayList<Position>();
		while (true) {
			ShipType t = ShipType.values()[(int)(Math.random() * 5)];
			for (Position p : P) {
				if (grid.getShip(p.x, p.y).type() == t) {
					pos.add(p);
				}
			}
			if (pos.size() > 0)
				break;
		}
		return pos;
	}

	private static int getSmallestShip(BattleshipGrid grid) {
		int l = 5;
		for (int i=4; i>=0; i--) {
			if (grid.statusOf(grid.ships()[i].positions()[0].x, grid.ships()[i].positions()[0].y) != PositionStatus.SUNK && grid.ships()[i].size() < l) {
				l = grid.ships()[i].size();
			}
		}
		return l;
	}
	
	private static boolean fits(Position pos, int size, BattleshipGrid grid) {
		boolean[] AXIS = new boolean[size];
		for (int j=0; j<size; j++)
			AXIS[j] = false;
		
		for (int axis=1; axis<size; axis++) {
			int c=0;
			boolean[] ors = new boolean[] { true, true, true, true };
			for (int o=0; o<4; o++) {
				Orientation orient = Orientation.values()[o];
				Position p;
				switch (orient) {
				case EAST:
					p = new Position(pos.x - axis, pos.y);
					for (int i=1; i<size; i++) {
						if (grid.isValid(p.x + i, p.y) && grid.statusOf(p.x + i, p.y) != PositionStatus.UNKNOWN)
							ors[0] = false;
						else if (!grid.isValid(p.x + i, p.y))
							ors[0] = false;
					}
					break;
				case NORTH:
					p = new Position(pos.x, pos.y + axis);
					for (int i=1; i<size; i++) {
						if (grid.isValid(p.x, p.y - i) && grid.statusOf(p.x, p.y - i) != PositionStatus.UNKNOWN)
							ors[1] = false;
						else if (!grid.isValid(p.x, p.y - i))
							ors[1] = false;
					}
					break;
				case SOUTH:
					p = new Position(pos.x, pos.y - axis);
					for (int i=1; i<size; i++) {
						if (grid.isValid(p.x, p.y + i) && grid.statusOf(p.x, p.y + i) != PositionStatus.UNKNOWN)
							ors[2] = false;
						else if (!grid.isValid(p.x, p.y + i))
							ors[2] = false;
					}
					break;
				case WEST:
					p = new Position(pos.x + axis, pos.y);
					for (int i=1; i<size; i++) {
						if (grid.isValid(p.x - i, p.y) && grid.statusOf(p.x - i, p.y) != PositionStatus.UNKNOWN)
							ors[3] = false;
						else if (!grid.isValid(p.x - i, p.y))
							ors[3] = false;
					}
					break;
				}
			}
			for (boolean b : ors) {
				if (b)
					AXIS[c] = true;
			}
			c++;
		}
		for (boolean b : AXIS) {
			if (b)
				return true;
		}
		return false;
	}
	
	//AI
	protected Battleship[] dumbPlace(BattleshipGrid grid) {
		Battleship[] ships = new Battleship[5];
		
		for (int i=0; i<5; i++) {
			int size = getSize(i);
			
			boolean valPos = false;
			while (!valPos) {
				Position pos = new Position((int)(Math.random() * 9), (int)(Math.random() * 9));
				int o = (int)(Math.random() * 3);
				int O = o;
				while (true) {	
					if (valid(pos, size, Orientation.values()[o], grid)) {
						ships[i] = new Battleship(ShipType.values()[i], grid, pos, Orientation.values()[o]);
						ships[i].regUpdate();
						grid.update(ships);
						valPos = true;
						break;
					} else {
						o++;
						if (o>3)
							o=0;
						if (O == o)
							break;
					}
				}
				if (valPos)
					break;
			}
		}
		return ships;
	}

	protected void dumbShoot(BattleshipGrid grid) {
		boolean hasHit = false;
		ArrayList<Position> hits = new ArrayList<Position>();
		for (int x=0; x<grid.posStatus.length; x++) {
			for (int y=0; y<grid.posStatus[x].length; y++) {
				if (grid.posStatus[x][y] == PositionStatus.HIT){
					hasHit = true;
					hits.add(new Position(x, y));
				}
			}
		}
		
		while(!hasHit) {
			Position p = new Position ((int)(Math.random() * 10), (int)(Math.random() * 10));
			if (grid.posStatus[p.x][p.y] == PositionStatus.UNKNOWN) {
				grid.shoot(p.x, p.y);
				break;
			}
		}
		while (hasHit) {
			if (hits.size() == 1) {
				Position a = hits.get(0).getAdjacent(Orientation.values()[(int)(Math.random() * 4)]);
				if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
					grid.shoot(a.x, a.y);
					break;
				}
			} else if (hits.size() > 1) {
				if (same(hits, grid)) { //if all hits are from the same ship
					boolean done = false;
					while (true) {
						Position p = hits.get((int)(Math.random() * hits.size()));
						if (isEnd(p, hits)) {
							Position a = p.getAdjacent(getEnd(p, hits));
							if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
								grid.shoot(a.x, a.y);
								done = true;
								break;
							}
						}
					}
					if (done)
						break;
				} else { //else if all hits are not from the same ship
					hits = getHits(hits, grid);
					boolean done = false;
					while (true) {
						Position p = hits.get((int)(Math.random() * hits.size()));
						if (isEnd(p, hits)) {
							Position a = p.getAdjacent(Orientation.values()[(int)(Math.random()*4)]);
							if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
								grid.shoot(a.x, a.y);
								done = true;
								break;
							}
						}
					}
					if (done)
						break;
				}
				
			}
		}
	}

	protected Battleship[] smartPlace(BattleshipGrid grid) {
		Battleship[] ships = new Battleship[5];
		
		for (int i=0; i<5; i++) {
			int size = getSize(i);
			
			boolean valPos = false;
			while (!valPos) {
				Position pos = new Position((int)(Math.random() * 9), (int)(Math.random() * 9));
				int o = (int)(Math.random() * 3);
				int O = o;
				while (true) {	
					if (valid(pos, size, Orientation.values()[o], grid) && shouldPlace(pos, size, Orientation.values()[o], grid)) {
						ships[i] = new Battleship(ShipType.values()[i], grid, pos, Orientation.values()[o]);
						ships[i].regUpdate();
						grid.update(ships);
						valPos = true;
						break;
					} else {
						o++;
						if (o>3)
							o=0;
						if (O == o)
							break;
					}
				}
				if (valPos)
					break;
			}
		}
		return ships;
	}
	
	protected void smartShoot(BattleshipGrid grid) {
		boolean hasHit = false;
		ArrayList<Position> hits = new ArrayList<Position>();
		for (int x=0; x<grid.posStatus.length; x++) {
			for (int y=0; y<grid.posStatus[x].length; y++) {
				if (grid.posStatus[x][y] == PositionStatus.HIT){
					hasHit = true;
					hits.add(new Position(x, y));
				}
			}
		}
		
		while(!hasHit) {
			Position p = new Position ((int)(Math.random() * 10), (int)(Math.random() * 10));
			if (grid.posStatus[p.x][p.y] == PositionStatus.UNKNOWN) {
				if (fits(p, getSmallestShip(grid), grid)) {
					grid.shoot(p.x, p.y);
					break;
				}
			}
		}
		while (hasHit) {
			if (hits.size() == 1) {
				Position a = hits.get(0).getAdjacent(Orientation.values()[(int)(Math.random() * 4)]);
				if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
					grid.shoot(a.x, a.y);
					break;
				}
			} else if (hits.size() > 1) {
				if (same(hits, grid)) { //if all hits are from the same ship
					boolean done = false;
					while (true) {
						Position p = hits.get((int)(Math.random() * hits.size()));
						if (isEnd(p, hits)) {
							Position a = p.getAdjacent(getEnd(p, hits));
							if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
								grid.shoot(a.x, a.y);
								done = true;
								break;
							}
						}
					}
					if (done)
						break;
				} else { //else if all hits are not from the same ship
					hits = getHits(hits, grid);
					boolean done = false;
					while (true) {
						Position p = hits.get((int)(Math.random() * hits.size()));
						if (isEnd(p, hits)) {
							Position a = p.getAdjacent(Orientation.values()[(int)(Math.random()*4)]);
							if (grid.isValid(a.x, a.y) && grid.statusOf(a.x, a.y) == PositionStatus.UNKNOWN) {
								grid.shoot(a.x, a.y);
								done = true;
								break;
							}
						}
					}
					if (done)
						break;
				}
				
			}
		}
	}
	
}
