package battleship;

import gameEngine.Game;
import gameEngine.Screen;
import gameEngine.ScreenFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MenuScreen extends Screen {
	
	private Game game;
	
	private Button play = new Button(25, 350, 400, 50, "play");
	private Button easy = new Button(25, 400, 133, 50, "easy");
	private Button medium = new Button(158, 400, 134, 50, "medium");
	private Button hard = new Button(292, 400, 133, 50, "hard");
	
	private Color background = new Color(0, 25, 153), text = new Color(0, 0, 51);
	private int diff = 0;

	public MenuScreen(ScreenFactory screenFactory) {
		super(screenFactory);
		game = getScreenFactory().getGame();
		play.setfSize(40);
		easy.setfSize(25); medium.setfSize(25); hard.setfSize(25);
		easy.setDown(true); medium.setDown(false); hard.setDown(false);
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onUpdate() {
		game.input().update();
		//button updates
		play.update(game.input());
		easy.update(game.input()); medium.update(game.input()); hard.update(game.input());
		
		if (play.clicked()) {
			//go to play screen
			game.getScreenFactory().showScreen(new GameScreen(game.getScreenFactory(), diff));
		}
		//difficulty
		if (easy.clicked()) {
			diff = 0;
			easy.setDown(true); medium.setDown(false); hard.setDown(false);
		} else if (medium.clicked()) {
			diff = 1;
			easy.setDown(false); medium.setDown(true); hard.setDown(false);
		} else if (hard.clicked()) {
			diff = 2;
			easy.setDown(false); medium.setDown(false); hard.setDown(true);
		}
		
		//button endUpdates
		play.endUpdate();
		easy.endUpdate(); medium.endUpdate(); hard.endUpdate();
		game.input().endUpdate();
	}

	@Override
	public void onDraw(Graphics2D g2d) {
		//background
		g2d.setColor(background);
		g2d.fillRect(0, 0, game.getFrame().getWidth(), game.getFrame().getHeight());
		
		//draw Battleship
		g2d.setColor(text);
		g2d.setFont(new Font("Stencil", Font.BOLD, 60));
		
		FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString("Battleship", (game.getFrame().getWidth() - fm.stringWidth("Battleship")) / 2, 150);
		
		play.draw(g2d);
		easy.draw(g2d); medium.draw(g2d); hard.draw(g2d);
	}

}
