package battleship;

public class Computer1 extends Enemy {

	@Override
	public Battleship[] placeShips(BattleshipGrid grid) {
		return dumbPlace(grid);
	}

	@Override
	public void shoot(BattleshipGrid grid) {
		dumbShoot(grid);
	}
}
