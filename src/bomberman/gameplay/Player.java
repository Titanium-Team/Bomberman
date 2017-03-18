package bomberman.gameplay;

import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class Player {

    //--- Settings
    private final static double COLLISION_WIDTH = .6;
    private final static double COLLISION_HEIGHT = .6;

    private final static float ACCELERATION_STEP = .2F;
    private final static float ACCELERATION_LIMIT = 1;
    private final static float ACCELERATION_TIMER = 0.1F;

    private final Map<Direction, Boolean> acceleratingDirections = new HashMap<>();
    private float accelerationTimer = ACCELERATION_TIMER;

    //--- Game
    private final GameStatistic gameStatistic = new GameStatistic();
    private final GameMap gameMap;

    //--- PlayerProperties
    private final String name;
    private double health;

    private final PlayerType playerType;
    private final PropertyRepository propertyRepository = new PropertyRepository(this);

    //--- Position
    private final Vector2 vector = new Vector2(0, 0);
    private float xX = 0;
    private float xY = 0;

    private final BoundingBox boundingBox;
    private FacingDirection facingDirection = FacingDirection.NORTH;

    //--- Stats
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

    public PropertyRepository getPropertyRepository() {
        return this.propertyRepository;
    }

    public GameStatistic getGameStatistic() {
        return this.gameStatistic;
    }

    public Tile getTile() {

        return this.gameMap.getTile(
                (int) this.boundingBox.getCenter().getX(),
                (int) this.boundingBox.getCenter().getY()
        );

    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void update(float delta) {

        this.accelerationTimer -= delta;

        //--- Accelerating
        if (accelerationTimer <= 0) {
            if (this.acceleratingDirections.getOrDefault(Direction.UP, false)) {
                this.move(Direction.UP);
            }

            if (this.acceleratingDirections.getOrDefault(Direction.DOWN, false)) {
                this.move(Direction.DOWN);
            }

            if (this.acceleratingDirections.getOrDefault(Direction.LEFT, false)) {
                this.move(Direction.LEFT);
            }

            if (this.acceleratingDirections.getOrDefault(Direction.RIGHT, false)) {
                this.move(Direction.RIGHT);
            }

            this.accelerationTimer = ACCELERATION_TIMER;

        }


        //--- Facing Direction
        FacingDirection facing = FacingDirection.from(this.vector);
        this.facingDirection = (facing == FacingDirection.DEFAULT ? this.facingDirection : facing);

        //--- Collision
        Location location = this.boundingBox.getCenter();
        this.boundingBox.move(this.vector.getX() * delta, this.vector.getY() * delta);

        FacingDirection direction = this.gameMap.checkCollision(this);

        switch (direction) {

            case SOUTH:
            case NORTH: {
                this.vector.setY(0);
                this.boundingBox.setCenter(
                    /*range(
                        this.gameMap.getMin().getBoundingBox().getMax().getX() + (this.gameMap.getWidth() / 2),
                        this.boundingBox.getCenter().getX(),
                        this.gameMap.getTi)*/0,
                    location.getY()
                );
            }
            break;

            case EAST:
            case WEST:
                this.vector.setX(0);
                this.boundingBox.setCenter(
                    location.getX(),
                    this.boundingBox.getCenter().getY()
                );
                break;

            case NORTH_EAST: //<--- All diagonal collisions
                this.vector.setX(0);
                this.vector.setY(0);
                    this.boundingBox.setCenter(location.getX(), location.getY());
                break;

            case DEFAULT:
                break;

            default:
                throw new IllegalStateException(String.format("Unknown collision direction: %s", direction.name()));

        }

        this.gameMap.checkInteraction(this);

    }

    public void keyUp(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.move(Direction.STOP_VERTICAL_MOVEMENT);
                this.acceleratingDirections.put(Direction.UP, false);
                this.acceleratingDirections.put(Direction.DOWN, false);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.move(Direction.STOP_HORIZONTAL_MOVEMENT);
                this.acceleratingDirections.put(Direction.LEFT, false);
                this.acceleratingDirections.put(Direction.RIGHT, false);
                break;

        }

    }

    public void keyDown(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.acceleratingDirections.put(Direction.UP, true);
                break;

            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.acceleratingDirections.put(Direction.LEFT, true);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
                this.acceleratingDirections.put(Direction.RIGHT, true);
                break;

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
                this.acceleratingDirections.put(Direction.DOWN, true);
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

        float limit = this.propertyRepository.<Float>get(PropertyTypes.SPEED_FACTOR) * ACCELERATION_LIMIT;

        switch (d) {

            case UP:
                this.xY = range(0, this.xY + ACCELERATION_STEP, limit);
                this.vector.setY((float) -accelerationCurve(this.xY));
                break;

            case LEFT:
                this.xX = range(0, this.xX + ACCELERATION_STEP, limit);
                this.vector.setX((float) -accelerationCurve(this.xX));
                break;

            case RIGHT:
                this.xX = range(0, this.xX + ACCELERATION_STEP, limit);
                this.vector.setX((float) accelerationCurve(this.xX));
                break;

            case DOWN:
                this.xY = range(0, this.xY + ACCELERATION_STEP, limit
                );
                this.vector.setY((float) accelerationCurve(this.xY));
                break;

            case STOP_HORIZONTAL_MOVEMENT:
                this.xX = 0;
                this.vector.setX(0);
                break;

            case STOP_VERTICAL_MOVEMENT:
                this.xY = 0;
                this.vector.setY(0);
                break;

        }
    }

    private static double accelerationCurve(float y) {
        return Math.exp(2 * y - 1);
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

            if (vY < 0 && vX == 0) {
                return FacingDirection.NORTH;
            } else if (vY < 0 && vX > 0) {
                return FacingDirection.NORTH_EAST;
            } else if (vY == 0 && vX > 0) {
                return FacingDirection.EAST;
            } else if (vY > 0 && vX > 0) {
                return FacingDirection.SOUTH_EAST;
            } else if (vY > 0 && vX == 0) {
                return FacingDirection.SOUTH;
            } else if (vY > 0 && vX < 0) {
                return FacingDirection.SOUTH_WEST;
            } else if (vY == 0 && vX < 0) {
                return FacingDirection.WEST;
            } else if (vY < 0 && vX < 0) {
                return FacingDirection.NORTH_WEST;
            }

            return FacingDirection.DEFAULT;

        }

    }

}
