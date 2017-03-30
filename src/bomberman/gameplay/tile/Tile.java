package bomberman.gameplay.tile;

import bomberman.Main;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.Explosion;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.tile.objects.PowerUpTypes;
import bomberman.gameplay.utils.BoundingBox;

import java.util.*;

public class Tile {

    private Map<Player, Float> players = new HashMap<>();

    private TileType tileType;
    private TileAbility tileAbility;
    private final BoundingBox boundingBox;

    private TileObject tileObject;

    //--- Abilities
    private long deltaTime = System.currentTimeMillis();
    private Tile connectedTile = null;
    private Player.FacingDirection treadMillDirection = null;

    private double health;

    public Tile(TileType tileType, TileAbility tileAbility, BoundingBox boundingBox) {

        assert !(tileType == null);
        assert !(tileAbility == null);
        assert !(boundingBox == null);

        this.tileAbility = tileAbility;
        this.tileType = tileType;
        this.health = tileType.getHealth();
        this.boundingBox = boundingBox;
        this.health = tileType.getHealth();

    }

    public TileType getTileType() {
        return this.tileType;
    }

    public Tile getConnectedTile() {
        return this.connectedTile;
    }

    public Player.FacingDirection getTreadMillDirection() {
        return this.treadMillDirection;
    }

    public TileAbility getTileAbility() {
        return this.tileAbility;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public double getHealth() {
        return this.health;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public TileObject getTileObject() {
        return this.tileObject;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isExploding() {
        return (this.tileObject instanceof Explosion);
    }

    public boolean canVisit(Player player) {
        return !(
                this.boundingBox.intersects(player.getBoundingBox()) &&
                    (
                        !(this.tileType.isWalkable()) ||
                        (this.tileObject instanceof Bomb && !((Bomb) this.tileObject).canVisit(player))
                    )
        );
    }

    public void spawn(TileObject tileObject) {

        assert !(tileObject == null);

        this.tileObject = tileObject;
    }

    public void setConnectedTile(Tile connectedTile) {
        assert this.tileAbility == TileAbility.TELEPORT;
        this.connectedTile = connectedTile;
    }

    public void setTreadMillDirection(Player.FacingDirection direction) {
        this.treadMillDirection = direction;
    }

    public void setTileAbility(TileAbility tileAbility) {
        this.tileAbility = tileAbility;
    }

    public void spawnPowerup(int lifespan) {
        //default lifespan = 15
        int random = (int) (Math.random() * 11);
        switch (random) {
            case 0:
            case 1:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.SPEEDUP));
                break;
            case 2:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.SPEEDDOWN));
                break;
            case 3:
            case 4:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.FIREUP));
                break;
            case 5:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.FIREDOWN));
                break;
            case 6:
            case 7:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.BOMBUP));
                break;
            case 8:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.BOMBDOWN));
                break;
            case 9:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.POWERBOMB));
                break;
            case 10:
                this.spawn(new PowerUp(this, lifespan, PowerUpTypes.SPIKEBOMB));
                break;
        }

    }

    public void destroyObject() {
        this.tileObject = null;
    }


    public void interact(Player player) {

        long deltaTime = (System.currentTimeMillis() - this.deltaTime);
        this.deltaTime = System.currentTimeMillis();

        if(this.players.containsKey(player)) {
            return;
        }

        if(!(this.tileAbility == TileAbility.NORMAL)) {
            switch (this.tileAbility) {

                case SLOW: {
                    this.players.put(player, player.getPropertyRepository().getValue(PropertyTypes.SPEED_FACTOR));
                    player.getPropertyRepository().setValue(PropertyTypes.SPEED_FACTOR, .5F);
                }
                break;

                case TELEPORT: {
                    player.getBoundingBox().setCenter(this.connectedTile.getBoundingBox().getCenter());
                }
                break;

                case RANDOM_TELEPORT: {
                    GameMap map = Main.instance.getGameplayManager().getCurrentSession().getGameMap();

                    boolean success = false;
                    while (!(success)) {
                        int x = (int) (Math.random() * map.getWidth());
                        int y = (int) (Math.random() * map.getHeight());
                        if (map.getTile(x, y).get().getTileType() == TileTypes.GROUND && map.getTile(x, y).get().getTileObject() == null) {
                            player.getBoundingBox().setCenter(map.getTile(x,y).get().getBoundingBox().getCenter());
                            success = true;
                        }
                    }
                }
                break;

                case TREADMILL: {

                    switch (this.treadMillDirection) {

                        case NORTH: {
                            player.getBoundingBox().move(0, .006F * Math.min(deltaTime, 2));
                        }
                        break;

                        case WEST: {
                            player.getBoundingBox().move(-(.006F * Math.min(deltaTime, 2)), 0);
                        }
                        break;

                        case EAST: {
                            System.out.println(deltaTime);
                            player.getBoundingBox().move(.006F * Math.min(deltaTime, 2), 0);
                        }
                        break;

                        case SOUTH: {
                            player.getBoundingBox().move(0, -(.006F * Math.min(deltaTime, 2)));
                        }
                        break;

                    }

                }
                break;

            }
        }

    }

    public void update(float delta) {


        //--- Abilities
        if(!(this.tileAbility == TileAbility.NORMAL)) {
            Iterator<Map.Entry<Player, Float>> iterator = this.players.entrySet().iterator();
            iterator.forEachRemaining(e -> {

                if (!(e.getKey().getTile() == this)) {

                    switch (this.tileAbility) {

                        case SLOW: {
                            e.getKey().getPropertyRepository().setValue(PropertyTypes.SPEED_FACTOR, e.getValue());
                        }
                        break;

                    }

                    iterator.remove();
                }

            });
        }

        //--- Tile Object
        if (this.tileObject == null) {
            return;
        }
        this.tileObject.update(delta);

    }

    @Override
    public Tile clone() {
        Tile tile = new Tile(this.tileType, this.tileAbility, new BoundingBox(
                this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(),
                this.boundingBox.getMax().getX(), this.boundingBox.getMax().getY()
        ));
        tile.setHealth(this.health);

        if(!(this.tileObject == null)) {
            tile.spawn(this.tileObject);
        }

        return tile;
    }

}
