package bomberman.gameplay;

import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Keyboard;

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

    private final PlayerType playerType;


    //--- Stats
    private float PLAYER_speedFactor = 1.0F;
    private int BOMB_blastRadius = 1;
    private int BOMB_amount = 1;

    public Player(PlayerType playerType, GameMap gameMap, String name, Location center) {

        this.playerType = playerType;

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

    public PlayerType getPlayerType() {
        return playerType;
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

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.move(Direction.STOP_HORIZONTAL_MOVEMENT);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.move(Direction.STOP_VERTICAL_MOVEMENT);
                break;

            case Keyboard.KEY_SPACE:
                break;

        }

    }

    public void keyDown(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.move(Direction.UP);
                break;

            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.move(Direction.LEFT);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
                this.move(Direction.RIGHT);
                break;

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
                this.move(Direction.DOWN);
                break;

            case Keyboard.KEY_SPACE: {

                Tile tile = this.getTile();
                //@TODO
                tile.spawn(new Bomb(tile, 10));

            }
            break;

        }

    }

    public void move(Direction d) {

        switch (d) {

            case UP:
                this.vector.setY(
                        range(0, -(this.vector.getY() + .2F), 3 * this.PLAYER_speedFactor)
                );
                break;

            case LEFT:
                this.vector.setX(
                        range(0, -(this.vector.getX() + .2F), 3 * this.PLAYER_speedFactor)
                );
                break;

            case RIGHT:
                this.vector.setX(
                        range(0, this.vector.getX() + .2F, 3 * this.PLAYER_speedFactor)
                );
                break;

            case DOWN:
                this.vector.setY(
                        range(0, this.vector.getY() + .2F, 3 * this.PLAYER_speedFactor)
                );
                break;

            case STOP_HORIZONTAL_MOVEMENT:
                this.vector.setY(0);
                break;

            case STOP_VERTICAL_MOVEMENT:
                this.vector.setX(0);
                break;

        }
    }

    private static float range(float min, float value, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public enum PlayerType {

        LOCAL,
        AI,
        NETWORK;


    }

    public enum Direction {

        LEFT,
        RIGHT,
        UP,
        DOWN,

        STOP_VERTICAL_MOVEMENT,
        STOP_HORIZONTAL_MOVEMENT

    }

}
