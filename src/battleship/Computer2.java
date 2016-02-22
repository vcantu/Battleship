package battleship;

public class Computer2 extends Enemy {

	@Override
	public Battleship[] placeShips(BattleshipGrid grid) {
		return smartPlace(grid);
	}

	@Override
	public void shoot(BattleshipGrid grid) {
		dumbShoot(grid);
	}

}
