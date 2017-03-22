package bomberman.gameplay;

import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileTypes;
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


    private final static float ACCELERATION_STEP = .06F;
    private final static float ACCELERATION_LIMIT = 1F;
    private final static float ACCELERATION_TIMER = 0.01F;


    private final Map<Direction, Boolean> acceleratingDirections = new HashMap<>();
    private float accelerationTimer = ACCELERATION_TIMER;

    //--- Game
    private final GameStatistic gameStatistic = new GameStatistic();
    private final GameMap gameMap;

    //--- PlayerProperties
    private final String name;


    private final PlayerType playerType;
    private final PropertyRepository propertyRepository = new PropertyRepository(this);

    //--- Position
    private final Vector2 vector = new Vector2(0, 0);
    private float xX = 0;
    private float xY = 0;

    private final BoundingBox boundingBox;
    private FacingDirection facingDirection = FacingDirection.NORTH;

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

        Location location = this.boundingBox.getMin();
        return this.gameMap.getTile(

                (int) Math.round(location.getX()),
                (int) Math.round(location.getY())

        ).get();

    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void loseHealth() {
        this.getPropertyRepository().setValue(
            PropertyTypes.HEALTH,
            this.getPropertyRepository().getValue(PropertyTypes.HEALTH) - 1
        );

        if(this.getPropertyRepository().getValue(PropertyTypes.HEALTH) > 0) {
            System.out.println("player health: " + this.getPropertyRepository().getValue(PropertyTypes.HEALTH));
            this.getPropertyRepository().setValue(PropertyTypes.INVINCIBILITY, 3F);
            this.respawn();
        }else{
            System.out.println(name + "is dead!");
            //spieler in die mitte
            //this.boundingBox.setCenter(this.gameMap.getTile(this.gameMap.getWidth()/2,this.gameMap.getHeight()/2).get().getBoundingBox().getCenter());
            //anstatt spieler in die mitte: player bild entfernen, bounding box entfernen -> noclip
        }
    }

    private void respawn(){
        int x = (int) (Math.random() * this.gameMap.getWidth());
        int y = (int) (Math.random() * this.gameMap.getHeight());
        if (this.gameMap.getTile(x, y).get().getTileType() == TileTypes.GROUND && this.gameMap.getTile(x, y).get().getTileObject() == null) {
            this.boundingBox.setCenter(this.gameMap.getTile(x,y).get().getBoundingBox().getCenter());
        } else {
            this.respawn();
        }
    }

    public void update(float delta) {
        //SIEHE LOSEHEALTH
        //if(this.getPropertyRepository().getValue(PropertyTypes.HEALTH)>0){
            this.accelerationTimer -= delta;

            //--- Accelerating
            if (this.accelerationTimer <= 0) {
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


            /**
             this.gameMap.checkInteraction(this);

             switch (this.gameMap.checkCollision(this)) {

             case LEFT:
             case RIGHT:
             this.vector.setX(0);
             this.boundingBox.setCenter(location.getX(),this.getBoundingBox().getCenter().getY());
             break;
             case UP:
             case DOWN:
             this.vector.setY(0);
             this.boundingBox.setCenter(this.getBoundingBox().getCenter().getX(),location.getY());
             break;
             **/
            Direction direction = this.gameMap.checkCollision(this);

            BoundingBox min = this.gameMap.getMin().get().getBoundingBox();
            BoundingBox max = this.gameMap.getMax().get().getBoundingBox();

            double minX = (min.getMax().getX() + (COLLISION_WIDTH / 2));
            double minY = (min.getMax().getY() + (COLLISION_HEIGHT / 2));

            double maxX = (max.getMin().getX() - (COLLISION_WIDTH / 2));
            double maxY = (max.getMin().getY() - (COLLISION_HEIGHT / 2));

            switch (direction) {

                case UP:
                case DOWN: {
                    this.vector.setY(0);
                    this.boundingBox.setCenter(
                            range(minX, this.boundingBox.getCenter().getX(), maxX),
                            range(minY, location.getY(), maxY)
                    );
                }
                break;

                case LEFT:
                case RIGHT: {
                    this.vector.setX(0);

                    this.boundingBox.setCenter(
                            range(minX, location.getX(), maxX),
                            range(minY, this.boundingBox.getCenter().getY(), maxY)
                    );
                }
                break;

                case STOP_VERTICAL_MOVEMENT:
                    this.boundingBox.setCenter(
                            range(minX, location.getX(), maxX),
                            range(minY, location.getY(), maxY)
                    );
                    this.vector.setY(0);
                    this.vector.setX(0);
                    break;

                case STOP_HORIZONTAL_MOVEMENT: break;

                default:
                    throw new IllegalStateException(String.format("Unknown collision direction: %s", direction.name()));

            }

            this.gameMap.checkInteraction(this);

            //--- Update INVINCIBILITY Timer
            this.getPropertyRepository().setValue(
                PropertyTypes.INVINCIBILITY,
                this.getPropertyRepository().getValue(PropertyTypes.INVINCIBILITY) - delta
            );
        //}

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

            case Keyboard.KEY_Q:
                GameMap.STICKY_WALLS_OR_BUGS = !GameMap.STICKY_WALLS_OR_BUGS;
                System.out.println(GameMap.STICKY_WALLS_OR_BUGS ? "YOU ENABLED STICKY WALLS!" : "YOU ENABLED AWESOME MOVEMENT BUGS!");
                break;

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

                int bombsLeft = (int) this.getPropertyRepository().getValue(PropertyTypes.BOMB_AMOUNT);
                if(tile.getTileObject() instanceof Bomb || bombsLeft <= 0) {
                    assert bombsLeft == 0;
                    return;
                }

                this.getPropertyRepository().setValue(PropertyTypes.BOMB_AMOUNT, (float) (bombsLeft - 1));
                tile.spawn(new Bomb(this, tile, 2));

            }
            break;

        }

    }

    public void move(Direction d) {

        float limit = this.propertyRepository.getValue(PropertyTypes.SPEED_FACTOR) * ACCELERATION_LIMIT;

        switch (d) {

            case UP: {

                this.xY = (this.xX > this.xY ? this.xX : range(0, this.xY + ACCELERATION_STEP, limit));
                this.vector.setY((float) -accelerationCurve(this.xY));

            }
            break;

            case LEFT: {

                this.xX = (this.xY > this.xX ? this.xY : range(0, this.xX + ACCELERATION_STEP, limit));
                this.vector.setX((float) -accelerationCurve(this.xX));

            }
            break;

            case RIGHT: {

                this.xX = (this.xY > this.xX ? this.xY : range(0, this.xX + ACCELERATION_STEP, limit));
                this.vector.setX((float) accelerationCurve(this.xX));

            }
            break;

            case DOWN: {

                this.xY = (this.xX > this.xY ? this.xX : range(0, this.xY + ACCELERATION_STEP, limit));
                this.vector.setY((float) accelerationCurve(this.xY));

            }
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

    private static double accelerationCurve(float value) {
        return Math.exp(2 * value - 1);
    }

    private static float range(float min, float value, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private static double range(double min, double value, double max) {
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
