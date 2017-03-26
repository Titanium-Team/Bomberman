package bomberman.gameplay.tile;

import bomberman.gameplay.Player;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.Explosion;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.tile.objects.PowerUpTypes;
import bomberman.gameplay.utils.BoundingBox;

public class Tile {

    private TileType tileType;
    private final BoundingBox boundingBox;

    private TileObject tileObject;
    private double health;

    public Tile(TileType tileType, BoundingBox boundingBox) {

        assert !(tileType == null);
        assert !(boundingBox == null);

        this.tileType = tileType;
        this.health = tileType.getHealth();
        this.boundingBox = boundingBox;
        this.health = tileType.getHealth();

    }

    public TileType getTileType() {
        return this.tileType;
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

    public void spawnPowerup() {
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.SPEEDUP));
                break;
            case 3:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.SPEEDDOWN));
                break;
            case 1:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.FIREUP));
                break;
            case 4:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.FIREDOWN));
                break;
            case 2:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.BOMBUP));
                break;
            case 5:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.BOMBDOWN));
                break;
        }
    }

    public void destroyObject() {
        this.tileObject = null;
    }

    public void update(float delta) {

        if (this.health <= 0 && !this.tileType.isWalkable()) {
            this.setTileType(TileTypes.GROUND);
        }

        if (this.tileObject == null) {
            return;
        }
        this.tileObject.update(delta);

    }

    @Override
    public Tile clone() {
        Tile tile = new Tile(this.tileType, new BoundingBox(
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
