package bomberman.gameplay;

public class Tile {

    private final TileType tileType;

    private TileObject tileObject;
    private double explodingTime = -1D;

    public Tile(TileType tileType) {

        assert !(tileType == null);
        this.tileType = tileType;

    }

    public TileType getTileType() {
        return this.tileType;
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

}
