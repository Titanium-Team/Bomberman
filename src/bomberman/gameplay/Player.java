package bomberman.gameplay;

import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Keyboard;

public class Player {

    //--- Settings
    private final static double COLLISION_WIDTH = .8;
    private final static double COLLISION_HEIGHT = .8;

    //--- Game
    private final GameStatistic gameStatistic = new GameStatistic();
    private final GameMap gameMap;

    //--- Properties
    private final String name;
    private double health;

    private final PlayerType playerType;


    //--- Position
    private final Vector2 vector = new Vector2(0, 0);
    private final BoundingBox boundingBox;
    private FacingDirection facingDirection = FacingDirection.NORTH;


    //--- Stats
    private float PLAYER_speedFactor = 1.0F;
    private int BOMB_blastRadius = 1;
    private int BOMB_amount = 1;

    public Player(PlayerType playerType, GameMap gameMap, String name, Location center) {

        this.playerType = playerType;

        this.gameMap = gameMap;
        this.name = name;

        this.boundingBox = new BoundingBox(
                new Location(center.getX() - (COLLISION_WIDTH / 2), center.getY() - (COLLISION_HEIGHT / 2)),
                new Location(center.getX() + (COLLISION_WIDTH / 2), center.getY() + (COLLISION_HEIGHT / 2))
        );

    }

    public String getName() {
        return this.name;
    }

    public double getHealth() {
        return health;
    }

    public FacingDirection getFacingDirection() {
        return this.facingDirection;
    }

    public Vector2 getVector() {
        return this.vector;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public GameStatistic getGameStatistic() {
        return this.gameStatistic;
    }

    public Tile getTile() {

        return this.gameMap.get(
                (int) Math.round(this.boundingBox.getMin().getX()),
                (int) Math.round(this.boundingBox.getMin().getY())
        );

    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void update(float delta) {

        //--- Facing Direction
        FacingDirection facing = FacingDirection.from(this.vector);
        this.facingDirection = (facing == FacingDirection.DEFAULT ? this.facingDirection : facing);

        //--- Collision
        Location location = this.boundingBox.getCenter();

        this.boundingBox.move(this.vector.getX() * delta, this.vector.getY() * delta);

        this.gameMap.checkInteraction(this);
        if(this.gameMap.checkCollision(this)) {
            this.boundingBox.setCenter(location);
        }

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
                tile.spawn(new Bomb(tile, 6, 2));

            }
            break;

        }

    }

    public void move(Direction d) {

        switch (d) {

            case UP:
                this.vector.setY(
                        range(-3 * this.PLAYER_speedFactor, -(this.vector.getY() + .6F), 0)
                );
                break;

            case LEFT:
                this.vector.setX(
                        range(-3 * this.PLAYER_speedFactor, -(this.vector.getX() + .6F), 0)
                );
                break;

            case RIGHT:
                this.vector.setX(
                        range(0, this.vector.getX() + .6F, 3 * this.PLAYER_speedFactor)
                );
                break;

            case DOWN:
                this.vector.setY(
                        range(0, this.vector.getY() + .6F, 3 * this.PLAYER_speedFactor)
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

    public enum FacingDirection {

        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST,

        DEFAULT;

        public static FacingDirection from(Vector2 vector) {

            float vX = vector.getX();
            float vY = vector.getY();

            if(vY < 0 && vX == 0) {
                return FacingDirection.NORTH;
            } else if(vY < 0 && vX > 0) {
                return FacingDirection.NORTH_EAST;
            } else if(vY == 0 && vX > 0) {
                return FacingDirection.EAST;
            } else if(vY > 0 && vX > 0) {
                return FacingDirection.SOUTH_EAST;
            } else if(vY > 0 && vX == 0) {
                return FacingDirection.SOUTH;
            } else if(vY > 0 && vX < 0) {
                return FacingDirection.SOUTH_WEST;
            } else if(vY == 0 && vX < 0) {
                return FacingDirection.WEST;
            } else if(vY < 0 && vX < 0) {
                return FacingDirection.NORTH_WEST;
            }

            return FacingDirection.DEFAULT;

        }

    }

}
