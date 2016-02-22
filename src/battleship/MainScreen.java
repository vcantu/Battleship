package battleship;

import gameEngine.Game;
import gameEngine.Screen;
import gameEngine.ScreenFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MainScreen extends Screen {

	Game game;
	Color background = new Color(0, 25, 153), text = new Color(0, 0, 51);
	int counter = 0;
	boolean dispText = true;
	
	public MainScreen(ScreenFactory screenFactory) {
		super(screenFactory);
		game = getScreenFactory().getGame();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onUpdate() {
		if (game.input().leftPressed() && !game.input().prevLeftPressed()) {
			game.getScreenFactory().showScreen(new MenuScreen(game.getScreenFactory()));
		}
		counter++;
		
		if (counter == 50) {
			counter = 0;
			if (dispText)
				dispText = false;
			else
				dispText = true;
		}
		game.input().endUpdate();
	}

	@Override
	public void onDraw(Graphics2D g2d) {
		//Draw Background
		g2d.setColor(background);
		g2d.fillRect(0, 0, game.getFrame().getWidth(), game.getFrame().getHeight());
		
		//draw Battleship
		g2d.setColor(text);
		g2d.setFont(new Font("Stencil", Font.BOLD, 60));
		
		FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString("Battleship", (game.getFrame().getWidth() - fm.stringWidth("Battleship")) / 2, 350);
		
		//draw click to play
		if (dispText) {
			g2d.setFont(new Font("Stencil", Font.BOLD, 40));
			fm = g2d.getFontMetrics();
	        g2d.drawString("Click to Play", (game.getFrame().getWidth() - fm.stringWidth("Click to Play")) / 2, 400);
		}
	}

}
