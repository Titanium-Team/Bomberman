package bomberman.gameplay.tile;

import bomberman.gameplay.Player;

public abstract class TileObject {

    private final Player owner;
    private final Tile parent;

    private float lifespan;

    public TileObject(Player owner, Tile parent, float lifespan) {

        assert !(owner == null);
        assert !(parent == null);

        this.owner = owner;
        this.parent = parent;
        this.lifespan = lifespan;

    }

    public Player getOwner() {
        return this.owner;
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
            this.execute();

            //--- destroy
            this.parent.destroyObject();
        }
    }

}
