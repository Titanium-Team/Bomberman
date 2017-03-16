package bomberman.gameplay.tile;

import bomberman.gameplay.Player;

public abstract class TileObject {

    private final Player owner;
    private final Tile parent;

    private double lifespan;

    public TileObject(Player owner, Tile parent, double lifespan) {

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

    public double getLifespan() {
        return this.lifespan;
    }

    public void setLifespan(double lifespan) {
        this.lifespan = lifespan;
    }

    public abstract void execute();

    public abstract void interact(Player player);

}
