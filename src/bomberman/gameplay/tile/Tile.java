package bomberman.gameplay.tile;

import bomberman.gameplay.utils.BoundingBox;

public class Tile {

    private final TileType tileType;
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

    public void destroyObject() {
        this.tileObject = null;
    }

    public void update(float delta) {

        if(this.tileObject == null) {
           return;
        }

        //--- Execute, if it runs out of time
        this.tileObject.setLifespan(this.tileObject.getLifespan() - delta);

        if(this.tileObject.getLifespan() <= 0) {
            this.tileObject.execute();

            //--- destroy
            this.destroyObject();
        }


    }

}
