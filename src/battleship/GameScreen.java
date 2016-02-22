package battleship;

import gameEngine.Game;
import gameEngine.Screen;
import gameEngine.ScreenFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class GameScreen extends Screen {

	private Game game;
	private BattleshipGrid enemyGrid = new BattleshipGrid(new Point(99 + 25, 85), 10, 10);
	private BattleshipGrid playerGrid = new BattleshipGrid(new Point(99 + 25, 320 + 101 + 25), 10, 10);
	
	private boolean canPlay, playing = false, gameOver = false, won;
	
	//Game Vars
	private Rectangle currRect;
	private Color background = new Color(0, 25, 153);
	
	//Ships
	private Battleship[] playerShips = new Battleship[5];
	private Battleship[] enemyShips = new Battleship[5];
	
	//Buttons					
	private Button begin = new Button(20, 409, 80, 32, "BEGIN");
	
	private Button pWins = new Button(25, 350, 400, 50, "Player Wins");
	private Button cWins = new Button(25, 350, 400, 50, "Player Loses");
	
	private Enemy enemy;
	
	public GameScreen(ScreenFactory screenFactory, int diff) {
		super(screenFactory);
		game = getScreenFactory().getGame();
		for (int i=0; i<5; i++) {
			playerShips[i] = new Battleship(ShipType.values()[i], playerGrid);
		}
		pWins.setfSize(40);
		cWins.setfSize(40);
		begin.setfSize(20);
		if (diff == 0)
			enemy = new Computer1();
		else if (diff == 1)
			enemy = new Computer2();
		else if (diff == 2)
			enemy = new Computer3();
		//else
			//multiplayer
	}

	@Override
	public void onCreate() {
	}
	
	@Override
	public void onUpdate() {
		game.input().update();
		currRect = getRectangleOnMouse();
		
		if (!playing) {
			canPlay = true;
			playerGrid.update(playerShips);
			for (Battleship b : playerShips) {
				if (!b.onGrid())
					canPlay = false;
				b.Update(this);
			}
			if (canPlay) {
				begin.update(game.input());
				if (begin.clicked()) {
					//set up for game
					enemyShips = enemy.placeShips(enemyGrid);
					for (Battleship b : enemyShips) {
						b.regUpdate();
					}
					playing = true;
				}
			}
		} else if (playing && !gameOver){ //Playing
			Position mousePos = positionOnMouse(enemyGrid);
			if (mousePos != null && game.input().leftPressed() && !game.input().prevLeftPressed()) {
				if (enemyGrid.statusOf(mousePos.x, mousePos.y) == PositionStatus.UNKNOWN) {
					enemyGrid.shoot(mousePos.x, mousePos.y);
					enemyGrid.postUpdate(enemyShips);
					if (gridLost(enemyGrid)) {
						//player has won
						won = true;
						gameOver = true;
					}
					enemy.shoot(playerGrid);
					playerGrid.postUpdate(playerShips);
					if (gridLost(playerGrid)) {
						//computer has won
						won = false;
						gameOver = true;
					}
				}
			}
		} else { // game over
			if (won) {
				pWins.update(game.input());
				if (pWins.clicked())
					game.getScreenFactory().showScreen(new MenuScreen(game.getScreenFactory()));
			} else {
				cWins.update(game.input());
				if (cWins.clicked())
					game.getScreenFactory().showScreen(new MenuScreen(game.getScreenFactory()));
			}
		}
		game.input().endUpdate();
	}

	@Override
	public void onDraw(Graphics2D g2d) {		
		//background
		g2d.setColor(background);
		g2d.fillRect(0, 0, game.getFrame().getWidth(), game.getFrame().getHeight());
		
		if (currRect != null) {
			g2d.setColor(Color.RED);
			g2d.fillRect(currRect.x, currRect.y, currRect.width, currRect.height);
		}
		//Game Draws
		playerGrid.draw(g2d, Color.WHITE);
		enemyGrid.draw(g2d, Color.WHITE);
		
		//setup
		canPlay = true;
		for (Battleship b : playerShips) {
			if (!b.onGrid())
				canPlay = false;
		}
		if (!playing && canPlay)
			begin.draw(g2d);
		
		//playing
		if (playing) {
			for (Battleship b : enemyShips) {
				if (enemyGrid.statusOf(b.positions()[0].x, b.positions()[0].y) == PositionStatus.SUNK)
					b.Draw(g2d);
			}
		}
		
		//ships
		for (Battleship b : playerShips) {
			b.Draw(g2d);
		}
		
		//draw hits
		for (int x=0; x<enemyGrid.GRID().length; x++) {
			for (int y=0; y<enemyGrid.GRID()[x].length; y++) {
				if (enemyGrid.posStatus[x][y] == PositionStatus.HIT) {
					Rectangle r = enemyGrid.GRID()[x][y];
					g2d.setColor(Color.RED);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				} else if (enemyGrid.posStatus[x][y] == PositionStatus.MISS) {
					Rectangle r = enemyGrid.GRID()[x][y];
					g2d.setColor(Color.WHITE);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				} else if (enemyGrid.posStatus[x][y] == PositionStatus.SUNK) {
					Rectangle r = enemyGrid.GRID()[x][y];
					g2d.setColor(Color.PINK);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				}
			}
		}
		for (int x=0; x<playerGrid.GRID().length; x++) {
			for (int y=0; y<playerGrid.GRID()[x].length; y++) {
				if (playerGrid.posStatus[x][y] == PositionStatus.HIT) {
					Rectangle r = playerGrid.GRID()[x][y];
					g2d.setColor(Color.RED);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				} else if (playerGrid.posStatus[x][y] == PositionStatus.MISS) {
					Rectangle r = playerGrid.GRID()[x][y];
					g2d.setColor(Color.WHITE);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				} else if (playerGrid.posStatus[x][y] == PositionStatus.SUNK) {
					Rectangle r = playerGrid.GRID()[x][y];
					g2d.setColor(Color.PINK);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				}
			}
		}
		
		if (gameOver) {
			if (won) {
				pWins.draw(g2d);
			} else {
				cWins.draw(g2d);
			}
		}
	}
	
	public Rectangle getRectangleOnMouse() {
		Point mouse = new Point(game.input().mouseX(), game.input().mouseY());
		for (Rectangle[] R : playerGrid.GRID()) {
			for (Rectangle r : R) {
				if (r.contains(mouse)) {
					return r;
				}
			}
		}
		for (Rectangle[] R : enemyGrid.GRID()) {
			for (Rectangle r : R) {
				if (r.contains(mouse)) {
					return r;
				}
			}
		}
		return null;
	}
	
	public Position positionOnMouse(BattleshipGrid grid) {
		Point mouse = new Point(game.input().mouseX(), game.input().mouseY());
		for (int x=0; x<grid.GRID().length; x++) {
			for (int y=0; y<grid.GRID()[x].length; y++) {
				if (grid.GRID()[x][y].contains(mouse)) {
					return new Position(x, y);
				}
			}
		}
		return null;
	}
	
	public Battleship[] playerShips() { return playerShips; }

	public boolean gridLost(BattleshipGrid grid) {
		int c = 0;
		for (int x=0; x<grid.GRID().length; x++) {
			for (int y=0; y<grid.GRID()[x].length; y++) {
				if (grid.statusOf(x, y) == PositionStatus.SUNK)
					c++;
			}
		}
		return c == 17;
	}
}



