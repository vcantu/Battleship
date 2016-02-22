package battleship;

public class Computer3 extends Enemy {

	@Override
	public Battleship[] placeShips(BattleshipGrid grid) {
		return smartPlace(grid);
	}

	@Override
	public void shoot(BattleshipGrid grid) {
		smartShoot(grid);	
	}

}
