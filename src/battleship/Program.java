package battleship;

import gameEngine.Game;

public class Program {

	private Game game;
	
	public Program() {
		game = new Game(426 + 30, 800, "Battleship");
		game.getScreenFactory().showScreen(new MainScreen(game.getScreenFactory()));
	}
	
	public static void main(String[] args) {
		new Program();
	}
}