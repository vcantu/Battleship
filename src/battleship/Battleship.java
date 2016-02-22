package battleship;

import gameEngine.Input;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Battleship {
	
	//for constructor
	private ShipType type;
	private BattleshipGrid grid;
	
	//Per Ship
	private int size;
	private Point point; //location on screen
	private Rectangle rect;
	
	//No set Required
	private Position position;//position on grid
	
	//Already Set
	private Orientation orientation = Orientation.NORTH;//orientation of ship(south when game starts)
	private boolean onGrid = false;						//south means point is on top left
	
	//other varables
	private boolean attachedToMouse = false;
	private int diffX, diffY;//for mouse
	private Position[] positions;//points on grid
	private Rectangle[] rects;
	private int axis;
	private GameScreen screen;
	
	
	public Battleship(ShipType type, BattleshipGrid grid) {
		this.type = type;
		this.grid = grid;
		switch (type) {
			case Battleship:
				size = 4;
				point = new Point(10, 460 + (32 * 5) + 10 + (32 * (size - 1)));
				rect = new Rectangle(10, 460 + (32 * 5) + 10, 32, 32 * 4);
				break;
			case Carrier:
				size = 5;
				point = new Point(10, 460 + (32 * (size - 1)));
				rect = new Rectangle(10, 460, 32, 32 * 5);
				break;
			case Cruiser:
				size = 3;
				point = new Point(10 + 32 + 10, 460 + (32 * (size - 1)));
				rect = new Rectangle(10 + 32 + 10, 460, 32, 32 * 3);
				break;
			case Destroyer:
				size = 2;
				point = new Point(10 + 32 + 10, 460 + (32 * 6) + 20 + (32 * (size - 1)));
				rect = new Rectangle(10 + 32 + 10, 460 + (32 * 6) + 20, 32, 32 * 2);
				break;
			case Submarine:
				size = 3;
				point = new Point(10 + 32 + 10, 460 + (32 * 3) + 10 + (32 * (size - 1)));
				rect = new Rectangle(10 + 32 + 10, 460 + (32 * 3) + 10, 32, 32 * 3);
				break;
		}
		positions = new Position[size];
		rects = new Rectangle[size];
		for (int i=0; i<size; i++) {
			rects[i] = new Rectangle(point.x, point.y + (32 * i), 32, 32);
		}
	}
	
	public Battleship(ShipType type, BattleshipGrid grid, Position pos, Orientation o) {
		this.type = type;
		this.grid = grid;
		switch (type) {
			case Battleship:
				size = 4;
				break;
			case Carrier:
				size = 5;
				break;
			case Cruiser:
				size = 3;
				break;
			case Destroyer:
				size = 2;
				break;
			case Submarine:
				size = 3;
				break;
		}
		position = pos;
		positions = new Position[size];
		point = grid.GRID()[position.x][position.y].getLocation();
		
		rects = new Rectangle[size];
		orientation = o;
		onGrid = true;
	}
	
	public void regUpdate() {
		//set rectangles
		switch (orientation) {
			case EAST:
				rect = new Rectangle(point.x, point.y, 32 * size, 32);
				for (int i=0; i<size; i++) {
					rects[i] = new Rectangle(rect.x + (32 * i), rect.y, 32, 32);
				}
				break;
			case NORTH:
				rect = new Rectangle(point.x, point.y - 32 * (size - 1), 32, 32 * size);
				for (int i=0; i<size; i++) {
					rects[i] = new Rectangle(rect.x, rect.y + (32 * (size-1 -i)), 32, 32);
				}
				break;
			case SOUTH:
				rect = new Rectangle(point.x, point.y, 32, 32 * size);
				for (int i=0; i<size; i++) {
					rects[i] = new Rectangle(rect.x, rect.y + (32 * i), 32, 32);
				}
				break;
			case WEST:
				rect = new Rectangle(point.x - 32 * (size - 1), point.y, 32 * size, 32);
				for (int i=0; i<size; i++) {
					rects[i] = new Rectangle(rect.x  + (32 * (size-1 -i)), rect.y, 32, 32);
				}
				break;
		}
		
		//set positions
		if (onGrid && position != null) {
			positions[0] = position;
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
		}
	}
	
	public void Update(GameScreen gameScreen) {
		screen = gameScreen;
		Input in = screen.getScreenFactory().getGame().input();
		if (position != null && grid.isValid(position.x, position.y)) {
			point = grid.GRID()[position.x][position.y].getLocation();
		}
		
		if (in.leftPressed() && !in.prevLeftPressed()) {
			if (rect.contains(in.mouseX(), in.mouseY())) {
				for (int i=0; i<size; i++) {
					if (rects[i].contains(in.mouseX(), in.mouseY())) {
						axis = i;
					}
				}
				for (Battleship b : screen.playerShips()) {
					b.detach();
				}
				attachedToMouse = true;
				diffX = point.x - in.mouseX();
				diffY = point.y - in.mouseY();
				positions = new Position[size];
				position = null;
			} else {
				attachedToMouse = false;
				axis = -1;
			}
		}
		if (attachedToMouse) {
			point = new Point(in.mouseX() + diffX, in.mouseY() + diffY);
			onGrid = false;
		}

		if (attachedToMouse && !in.leftPressed() && !onGrid) {
			snapToGrid(in);
			attachedToMouse = false;
		}
		
		this.regUpdate();
		
		if (!attachedToMouse && in.rightPressed() && !in.prevRightPressed() && onGrid) {
			for (int i=0; i<size; i++) {
				if (rects[i].contains(in.mouseX(), in.mouseY())) {
					rotate(i);
				}
			}
		}
		
	}
	
	public void Draw(Graphics g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
	
	public void rotate(int axis) {
		Position AXIS;
		switch (orientation) {
			case EAST:
				AXIS = new Position(position.x + axis, position.y);
				break;
			case NORTH:
				AXIS = new Position(position.x, position.y - axis);
				break;
			case SOUTH:
				AXIS = new Position(position.x - axis, position.y);
				break;
			case WEST:
				AXIS = new Position(position.x, position.y + axis);
				break;
			default:
				AXIS = position;
				break;
		}
		int i = orientation.ordinal(); i++;
		if (i < 0) { i = 3; }
		if (i > 3) { i = 0; }
		
		Position[] P = new Position[size];	
		if (i == 0) {//north
			for (int c=0; c<size; c++) {
				P[c] = new Position(AXIS.x, AXIS.y + axis - c);
			}
		} else if (i == 1) {//west
			for (int c=0; c<size; c++) {
				P[c] = new Position(AXIS.x + axis - c, AXIS.y);
			}
		} else if (i == 2) {//south
			for (int c=0; c<size; c++) {
				P[c] = new Position(AXIS.x - axis, AXIS.y - axis - axis + c);
			}
		} else if (i == 3) {//east
			for (int c=0; c<size; c++) {
				P[c] = new Position(AXIS.x + c, AXIS.y + axis);
			}
		} 
		
		boolean canTurn = true;
		for (int c=0; c<size; c++) {
			if (grid.isValid(positions[c].x, positions[c].y))
				grid.setSpace(positions[c].x, positions[c].y, false);
		}
		
		for (int c=0; c<size; c++) {
			if (!grid.isValid(P[c].x, P[c].y))
				canTurn = false;
			else
				if (!grid.isEmpty(P[c].x, P[c].y))
					canTurn = false;
		}
		
		if (canTurn) {
			onGrid = true;
			orientation = Orientation.values()[i];
			switch (orientation) {
				case EAST:
					position = new Position(AXIS.x, AXIS.y + axis);
					break;
				case NORTH:
					position = new Position(AXIS.x, AXIS.y + axis);	
					break;
				case SOUTH:
					position = new Position(AXIS.x - axis, AXIS.y - axis - axis);	
					break;
				case WEST:
					position = new Position(AXIS.x + axis, AXIS.y);						
					break;
			}
		} else
			onGrid = true;
	}	

	public void snapToGrid(Input in) {
		//loop through grid squares
		for (int x=0; x<grid.GRID().length; x++) {
		for (int y=0; y<grid.GRID()[x].length; y++) {
			if (grid.GRID()[x][y].contains(new Point(in.mouseX(), in.mouseY()))) {
				switch (orientation) {
					case EAST:	
						positions[0] = new Position(x - axis, y);
						for (int i=1; i<size; i++) {
							positions[i] = new Position(positions[i-1].x + 1, positions[i-1].y);
						}
						break;
					case NORTH:
						positions[0] = new Position(x, y + axis);
						for (int i=1; i<size; i++) {
							positions[i] = new Position(positions[i-1].x, positions[i-1].y - 1);
						}
						break;
					case SOUTH:
						positions[0] = new Position(x, y - axis);
						for (int i=1; i<size; i++) {
							positions[i] = new Position(positions[i-1].x, positions[i-1].y + 1);
						}
						break;
					case WEST:
						positions[0] = new Position(x + axis, y);
						for (int i=1; i<size; i++) {
							positions[i] = new Position(positions[i-1].x - 1, positions[i-1].x);
						}
						break;
				}
				boolean valid = true;
				for (Position p : positions) {
					if (!grid.isValid(p.x, p.y) || !grid.isEmpty(p.x, p.y) )
						valid = false;
				}
				if (valid) {
					point = grid.GRID()[positions[0].x][positions[0].y].getLocation();
					position = new Position(positions[0].x, positions[0].y);
					onGrid = true;
				} else
					onGrid = false;
			}
		}
		}
	}
	
	public Position[] positions() { return positions; }
	
	public boolean onGrid() { return onGrid; }
	public Rectangle rect() { return rect; }
	
	public void detach() { attachedToMouse = false; }
	
	public ShipType type() { return type; }
	
	public int size() { return size; }
}








