package bomberman.gameplay;

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

    private final static float ACCELERATION_STEP = .5F;
    private final static float ACCELERATION_LIMIT = 1;

    private final Map<Direction, Boolean> acceleratingDirections = new HashMap<>();
    private float accelerationTimer = .01F;

    //--- Game
    private final GameStatistic gameStatistic = new GameStatistic();
    private final GameMap gameMap;

    //--- PlayerProperties
    private final String name;
    private double health;

    private final PlayerType playerType;

    //--- Position
    private final Vector2 vector = new Vector2(0, 0);
    private float xX = 0;
    private float xY = 0;

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

    //--- Test @TODO
    private boolean EXPERIMENTAL_MOVEMENT = true;

    public void update(float delta) {

        this.accelerationTimer -= delta;

        //--- Accelerating
        if(EXPERIMENTAL_MOVEMENT && accelerationTimer <= 0) {
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

            System.out.println(this.vector.getX() + " - " + this.vector.getY());

            accelerationTimer = .3F;
        }


        //--- Facing Direction
        FacingDirection facing = FacingDirection.from(this.vector);
        this.facingDirection = (facing == FacingDirection.DEFAULT ? this.facingDirection : facing);

        //--- Collision
        Location location = this.boundingBox.getCenter();

        this.boundingBox.move(this.vector.getX() * delta, this.vector.getY() * delta);

        this.gameMap.checkInteraction(this);
        if (this.gameMap.checkCollision(this)) {
            this.boundingBox.setCenter(location);
        }

    }

    public void keyUp(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.move(Direction.STOP_VERTICAL_MOVEMENT);

                if(EXPERIMENTAL_MOVEMENT) {
                    this.acceleratingDirections.put(Direction.UP, false);
                    this.acceleratingDirections.put(Direction.DOWN, false);
                }
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.move(Direction.STOP_HORIZONTAL_MOVEMENT);

                if(EXPERIMENTAL_MOVEMENT) {
                    this.acceleratingDirections.put(Direction.LEFT, false);
                    this.acceleratingDirections.put(Direction.RIGHT, false);
                }
                break;

            case Keyboard.KEY_SPACE:
                break;

        }

    }

    public void keyDown(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                if(EXPERIMENTAL_MOVEMENT)
                    this.acceleratingDirections.put(Direction.UP, true);
                else
                    this.move(Direction.UP);
                break;

            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                if(EXPERIMENTAL_MOVEMENT)
                    this.acceleratingDirections.put(Direction.LEFT, true);
                else
                    this.move(Direction.LEFT);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
                if(EXPERIMENTAL_MOVEMENT)
                    this.acceleratingDirections.put(Direction.RIGHT, true);
                else
                    this.move(Direction.RIGHT);
                break;

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
                if(EXPERIMENTAL_MOVEMENT)
                    this.acceleratingDirections.put(Direction.DOWN, true);
                else
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
                this.xY = range(0, this.xY + ACCELERATION_STEP, ACCELERATION_LIMIT * this.PLAYER_speedFactor);
                this.vector.setY((float) -a(this.xY));
                break;

            case LEFT:
                this.xX = range(0, this.xX + ACCELERATION_STEP, ACCELERATION_LIMIT * this.PLAYER_speedFactor);
                this.vector.setX((float) -a(this.xX));
                break;

            case RIGHT:
                this.xX = range(0, this.xX + ACCELERATION_STEP, ACCELERATION_LIMIT * this.PLAYER_speedFactor);
                this.vector.setX((float) a(this.xX));
                break;

            case DOWN:
                this.xY = range(0, this.xY + ACCELERATION_STEP, ACCELERATION_LIMIT * this.PLAYER_speedFactor);
                this.vector.setY((float) a(this.xY));
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

    private static double a(float y) {
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
