package bomberman.gameplay;

public abstract class TileObject {

    private final Tile parent;
    private int lifespan;

    public TileObject(Tile parent, int lifespan) {

        assert !(parent == null);

        this.parent = parent;
        this.lifespan = lifespan;

    }


    public abstract void execute();

    public abstract void interact(Player player);

}
