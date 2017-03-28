package bomberman.gameplay;

import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LocalPlayer extends Player {

    //--- Settings
    private final static float ACCELERATION_STEP = .09F;
    private final static float ACCELERATION_LIMIT = 1.1F;
    private final static float ACCELERATION_TIMER = 0.01F;

    private final Map<Direction, KeyEntry> acceleratingDirections = new HashMap<>();
    private float accelerationTimer = ACCELERATION_TIMER;

    //--- Position
    private FacingDirection facingDirection = FacingDirection.NORTH;
    private Location lastLocation;
    private Direction[] direction = new Direction[2];

    private final Vector2 vector = new Vector2(0, 0);
    private float xX = 0;
    private float xY = 0;

    public LocalPlayer(GameSession gameSession, String name, Location center) {
        super(gameSession, Player.PlayerType.LOCAL, name, center);

        this.lastLocation = center;

        //--- Populate map w/ default values
        Stream.of(Direction.values()).forEach(e -> this.acceleratingDirections.put(e, new KeyEntry()));
    }

    public Direction[] getDirection() {
        return this.direction;
    }

    @Override
    public FacingDirection getFacingDirection() {
        return this.facingDirection;
    }

    @Override
    public void update(float delta) {

        //--- Distance Travelled
        if(this.isAlive()) {
            this.getGameStatistic().update(Statistics.DISTANCE_TRAVELLED, this.lastLocation.distanceTo(this.getBoundingBox().getCenter()));
            this.lastLocation = this.getBoundingBox().getCenter();
        }

        //--- Accelerating
        this.accelerationTimer -= delta;

        if (this.accelerationTimer <= 0) {
            if (this.acceleratingDirections.get(Direction.UP).isPressed()) {
                this.move(Direction.UP);
            }

            if (this.acceleratingDirections.get(Direction.DOWN).isPressed()) {
                this.move(Direction.DOWN);
            }

            if (this.acceleratingDirections.get(Direction.LEFT).isPressed()) {
                this.move(Direction.LEFT);
            }

            if (this.acceleratingDirections.get(Direction.RIGHT).isPressed()) {
                this.move(Direction.RIGHT);
            }

            this.accelerationTimer = ACCELERATION_TIMER;

        }

        //--- Facing Direction
        FacingDirection facing = FacingDirection.from(this.vector);
        this.facingDirection = (facing == FacingDirection.DEFAULT ? this.facingDirection : facing);

        //--- Collision
        Location location = this.getBoundingBox().getCenter();
        this.getBoundingBox().move(this.vector.getX() * delta, this.vector.getY() * delta);

        Player.Direction direction = this.getGameSession().getGameMap().checkCollision(this);

        BoundingBox min = this.getGameSession().getGameMap().getMin().get().getBoundingBox();
        BoundingBox max = this.getGameSession().getGameMap().getMax().get().getBoundingBox();

        double minX = (min.getMax().getX() + (COLLISION_WIDTH / 2));
        double minY = (min.getMax().getY() + (COLLISION_HEIGHT / 2));

        double maxX = (max.getMin().getX() - (COLLISION_WIDTH / 2));
        double maxY = (max.getMin().getY() - (COLLISION_HEIGHT / 2));

        switch (direction) {

            case UP: {
                Tile tile = this.getGameSession().getGameMap().getTile(
                    (int) this.getTile().getBoundingBox().getCenter().getX(),
                    (int) (this.getTile().getBoundingBox().getCenter().getY() - 1)
                ).get();

                if(tile.canVisit(this) && this.getFacingDirection() == FacingDirection.NORTH) {
                    if(this.getBoundingBox().getCenter().getX() < this.getTile().getBoundingBox().getCenter().getX()) {
                        this.getBoundingBox().move(-(this.vector.getY() * delta), 0);
                    } else {
                        this.getBoundingBox().move((this.vector.getY() * delta), 0);
                    }
                } else {
                    this.vector.setY(0);

                    this.getBoundingBox().setCenter(
                        range(minX, this.getBoundingBox().getCenter().getX(), maxX),
                        range(minY, location.getY(), maxY)
                    );
                }
            }
            break;

            case DOWN: {
                Tile tile = this.getGameSession().getGameMap().getTile(
                        (int) this.getTile().getBoundingBox().getCenter().getX(),
                        (int) (this.getTile().getBoundingBox().getCenter().getY() + 1)
                ).get();

                if(tile.canVisit(this) && this.getFacingDirection() == FacingDirection.SOUTH) {
                    if(this.getBoundingBox().getCenter().getX() < this.getTile().getBoundingBox().getCenter().getX()) {
                        this.getBoundingBox().move((this.vector.getY() * delta), 0);
                    } else {
                        this.getBoundingBox().move(-(this.vector.getY() * delta), 0);
                    }
                } else {
                    this.vector.setY(0);

                    this.getBoundingBox().setCenter(
                            range(minX, this.getBoundingBox().getCenter().getX(), maxX),
                            range(minY, location.getY(), maxY)
                    );
                }
            }
            break;

            case LEFT: {
                Tile tile = this.getGameSession().getGameMap().getTile(
                        (int) this.getTile().getBoundingBox().getCenter().getX() - 1,
                        (int) (this.getTile().getBoundingBox().getCenter().getY())
                ).get();

                if(tile.canVisit(this) && this.getFacingDirection() == FacingDirection.WEST) {
                    if(this.getBoundingBox().getCenter().getY() < this.getTile().getBoundingBox().getCenter().getY()) {
                        this.getBoundingBox().move(0, -(this.vector.getX() * delta));
                    } else {
                        this.getBoundingBox().move(0, (this.vector.getX() * delta));
                    }
                } else {
                    this.vector.setX(0);

                    this.getBoundingBox().setCenter(
                        range(minX, location.getX(), maxX),
                        range(minY, this.getBoundingBox().getCenter().getY(), maxY)
                    );
                }
            }
            break;

            case RIGHT: {
                Tile tile = this.getGameSession().getGameMap().getTile(
                        (int) this.getTile().getBoundingBox().getCenter().getX() + 1,
                        (int) (this.getTile().getBoundingBox().getCenter().getY())
                ).get();

                if(tile.canVisit(this) && this.getFacingDirection() == FacingDirection.EAST) {
                    if(this.getBoundingBox().getCenter().getY() < this.getTile().getBoundingBox().getCenter().getY()) {
                        this.getBoundingBox().move(0, (this.vector.getX() * delta));
                    } else {
                        this.getBoundingBox().move(0, -(this.vector.getX() * delta));
                    }
                } else {
                    this.vector.setX(0);

                    this.getBoundingBox().setCenter(
                            range(minX, location.getX(), maxX),
                            range(minY, this.getBoundingBox().getCenter().getY(), maxY)
                    );
                }
            }
            break;

            case STOP_VERTICAL_MOVEMENT:
                this.getBoundingBox().setCenter(
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

        this.getGameSession().getGameMap().checkInteraction(this);

        //--- Update INVINCIBILITY Timer
        this.getPropertyRepository().setValue(
            PropertyTypes.INVINCIBILITY,
            this.getPropertyRepository().getValue(PropertyTypes.INVINCIBILITY) - delta
        );

    }

    public void keyUp(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.move(Direction.STOP_VERTICAL_MOVEMENT);
                this.acceleratingDirections.get(Direction.UP).setPressed(false);
                this.acceleratingDirections.get(Direction.DOWN).setPressed(false);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.move(Direction.STOP_HORIZONTAL_MOVEMENT);
                this.acceleratingDirections.get(Direction.LEFT).setPressed(false);
                this.acceleratingDirections.get(Direction.RIGHT).setPressed(false);
                break;

        }

    }

    public void keyDown(int keyCode, char c) {

        switch (keyCode) {

            case Keyboard.KEY_UP:
            case Keyboard.KEY_W:
                this.acceleratingDirections.get(Direction.UP).setPressed(true);
                this.keyUp(Keyboard.KEY_LEFT, 'L');
                break;

            case Keyboard.KEY_LEFT:
            case Keyboard.KEY_A:
                this.keyUp(Keyboard.KEY_UP, 'L');
                this.acceleratingDirections.get(Direction.LEFT).setPressed(true);
                break;

            case Keyboard.KEY_RIGHT:
            case Keyboard.KEY_D:
                this.keyUp(Keyboard.KEY_UP, 'L');
                this.acceleratingDirections.get(Direction.RIGHT).setPressed(true);
                break;

            case Keyboard.KEY_DOWN:
            case Keyboard.KEY_S:
                this.keyUp(Keyboard.KEY_LEFT, 'L');
                this.acceleratingDirections.get(Direction.DOWN).setPressed(true);
                break;

            case Keyboard.KEY_SPACE: {

                Tile tile = this.getTile();

                int bombsLeft = (int) this.getPropertyRepository().getValue(PropertyTypes.BOMB_AMOUNT);

                if(tile.getTileObject() instanceof Bomb || bombsLeft <= 0) {
                    assert bombsLeft == 0;
                    return;
                }

                this.getPropertyRepository().setValue(PropertyTypes.BOMB_AMOUNT, (float) (bombsLeft - 1));
                tile.spawn(new Bomb(this, tile, 2, 1));

                this.getGameStatistic().update(Statistics.BOMBS_PLANTED, 1);

            }
            break;

        }

    }

    public void move(Direction d) {

        if(d == Direction.LEFT || d == Direction.RIGHT) {
            this.direction[0] = d;
        } else if(d == Direction.UP || d == Direction.DOWN) {
            this.direction[1] = d;
        }
        float limit = this.getPropertyRepository().getValue(PropertyTypes.SPEED_FACTOR) * ACCELERATION_LIMIT;

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
        return Math.exp(2 * value - .9D);
    }

    private static float range(float min, float value, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private static double range(double min, double value, double max) {
        return Math.min(Math.max(value, min), max);
    }

    private static class KeyEntry {

        private long lastUsed = System.currentTimeMillis();
        private boolean pressed = false;

        public KeyEntry() {}

        public long getLastUsed() {
            return this.lastUsed;
        }

        public boolean isPressed() {
            return this.pressed;
        }

        public void setPressed(boolean pressed) {
            this.pressed = pressed;
            this.lastUsed = System.currentTimeMillis();
        }

    }

}
