package bomberman.gameplay;

import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Player {

    //--- Settings
    public final static double COLLISION_WIDTH = 1;
    public final static double COLLISION_HEIGHT = 1;

    private final UUID identifier = UUID.randomUUID();
    private int index;

    //--- Game
    private final GameStatistic gameStatistic = new GameStatistic();
    private final GameSession gameSession;

    //--- PlayerProperties
    private final String name;

    private final PlayerType playerType;
    private final PropertyRepository propertyRepository = new PropertyRepository(this);

    private final BoundingBox boundingBox;

    public Player(GameSession gameSession, PlayerType playerType, String name, Location center) {

        this.playerType = playerType;
        this.gameSession = gameSession;

        this.name = name;

        this.boundingBox = new BoundingBox(
                new Location(center.getX() - (COLLISION_WIDTH / 2), center.getY() - (COLLISION_HEIGHT / 2)),
                new Location(center.getX() + (COLLISION_WIDTH / 2), center.getY() + (COLLISION_HEIGHT / 2))
        );

    }

    public int getIndex(){

        if(this.index < 0) {
            throw new IllegalStateException("ASSIGN. AN. INDEX.");
        }

        //TODO JAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAN ICH BRAUCHE DAS
        //DOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONE - Jan
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlive() {
        return this.getPropertyRepository().getValue(PropertyTypes.HEALTH) > 0;
    }

    public double getHealth() {
        return this.getPropertyRepository().getValue(PropertyTypes.HEALTH);
    }

    public abstract FacingDirection getFacingDirection();

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
        return this.gameSession.getGameMap().getTile(

                (int) Math.round(location.getX()),
                (int) Math.round(location.getY())

        ).get();

    }

    public GameSession getGameSession() {
        return this.gameSession;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    public void respawn(){
        int x = (int) (Math.random() * this.gameSession.getGameMap().getWidth());
        int y = (int) (Math.random() * this.gameSession.getGameMap().getHeight());
        if (this.gameSession.getGameMap().getTile(x, y).get().getTileType() == TileTypes.GROUND && this.gameSession.getGameMap().getTile(x, y).get().getTileObject() == null) {
            this.boundingBox.setCenter(this.gameSession.getGameMap().getTile(x,y).get().getBoundingBox().getCenter());
        } else {
            this.respawn();
        }
    }

    public void loseHealth() {
        this.getPropertyRepository().setValue(
                PropertyTypes.HEALTH,
                this.getPropertyRepository().getValue(PropertyTypes.HEALTH) - 1
        );

        if(this.isAlive()) {
            System.out.println("player health: " + this.getPropertyRepository().getValue(PropertyTypes.HEALTH));
            this.getPropertyRepository().setValue(PropertyTypes.INVINCIBILITY, 3F);
            this.respawn();
        }else{
            System.out.println("gameover");
        }
    }

    public abstract void update(float delta);

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Player)) {
            return false;
        }

        return ((Player) o).identifier.equals(this.identifier);

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

    public enum PlayerType {

        LOCAL,
        AI,
        NETWORK

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
