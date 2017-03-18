package bomberman.gameplay.tile;

import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.tile.objects.PowerUpTypes;
import bomberman.gameplay.utils.BoundingBox;

public class Tile {

    private TileType tileType;
    private final BoundingBox boundingBox;

    private TileObject tileObject;
    private double explodingTime = -1D;

    public Tile(TileType tileType, BoundingBox boundingBox) {

        assert !(tileType == null);
        assert !(boundingBox == null);

        this.tileType = tileType;
        this.boundingBox = boundingBox;

    }

    public TileType getTileType() {
        return this.tileType;
    }

    public void setTileType(TileType tileType){
        this.tileType = tileType;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public TileObject getTileObject() {
        return this.tileObject;
    }

    public double getExplodingTime() {
        return this.explodingTime;
    }

    public boolean isExploding() {
        return this.explodingTime > 0;
    }

    public void setExplodingTime(double explodingTime) {
        this.explodingTime = explodingTime;
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
            case 4:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.SPEEDDOWN));
                break;
            case 2:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.FIREUP));
                break;
            case 5:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.FIREDOWN));
                break;
            case 3:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.BOMBUP));
                break;
            case 6:
                this.spawn(new PowerUp(this, 15, PowerUpTypes.BOMBDOWN));
                break;
        }
    }

    public void destroyObject() {
        this.tileObject = null;
    }

    public void update(float delta) {

        if (this.tileObject == null) {
            return;
        }

        this.tileObject.update(delta);

    }

}
