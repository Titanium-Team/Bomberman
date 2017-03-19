package bomberman.gameplay.tile;

import bomberman.gameplay.Player;

public abstract class TileObject {

    private final Tile parent;

    private float lifespan;

    public TileObject(Tile parent, float lifespan) {

        assert !(parent == null);

        this.parent = parent;
        this.lifespan = lifespan;

    }

    public Tile getParent() {
        return this.parent;
    }

    public float getLifespan() {
        return this.lifespan;
    }

    public void setLifespan(float lifespan) {
        this.lifespan = lifespan;
    }

    public abstract void execute();

    public abstract void interact(Player player);

    public void update(float delta) {
        //--- Execute, if it runs out of time
        this.lifespan -= delta;

        if (this.lifespan <= 0) {
            //--- destroy
            this.parent.destroyObject();

            this.execute();

        }
    }

}
