package bomberman.gameplay;

import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;

import java.awt.event.KeyEvent;

public class Player {

	private final static double COLLISION_WIDTH = .8;
	private final static double COLLISION_HEIGHT = .8;

	private final GameStatistic gameStatistic = new GameStatistic();
	private final GameMap gameMap;

	private final String name;
	private final BoundingBox boundingBox;

	private double health;

	private final Vector2 vector = new Vector2(0, 0);

	//--- Stats
	private double PLAYER_speedFactor = 1.0D;
	private int BOMB_blastRadius = 1;
	private int BOMB_amount = 1;

	public Player(GameMap gameMap, String name, Location center) {


		this.gameMap = gameMap;
		this.name = name;

		this.boundingBox = new BoundingBox(
				new Location(center.getX() - COLLISION_WIDTH, center.getY() - COLLISION_HEIGHT),
				new Location(center.getX() + COLLISION_WIDTH, center.getY() + COLLISION_HEIGHT)
		);

	}

	public String getName() {
		return this.name;
	}

	public Vector2 getVector() {
		return this.vector;
	}

	public GameStatistic getGameStatistic() {
		return this.gameStatistic;
	}

	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public double getHealth() {
		return health;
	}

	public Tile getTile() {

		return this.gameMap.get(
				(int) Math.round(this.boundingBox.getCenter().getX()),
				(int) Math.round(this.boundingBox.getCenter().getY())
		);

	}

	public void setHealth(double health) {
		this.health = health;
	}

	public void update(float delta) {

	}

	public void keyUp(int keyCode, char c) {

		switch (keyCode) {

			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				this.vector.setY(0);
				break;

			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				this.vector.setX(0);
				break;

			case KeyEvent.VK_SPACE:
				break;

		}

	}

	public void keyDown(int keyCode, char c) {

		switch (keyCode) {

			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				this.vector.setY(
						range(0, this.vector.getY() - .2F, 3)
				);
				break;

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				this.vector.setX(
						range(0, this.vector.getX() - .2F, 3)
				);
				break;

			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				this.vector.setX(
						range(0, this.vector.getX() + .2F, 3)
				);
				break;

			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				this.vector.setY(
						range(0, this.vector.getY() + .2F, 3)
				);
				break;

			case KeyEvent.VK_SPACE: {

				Tile tile = this.getTile();
				//@TODO
				tile.spawn(new Bomb(tile, 10));

			}
			break;

		}

	}

	private static float range(float min, float value, float max) {
		return Math.min(Math.max(value, min), max);
	}

}
