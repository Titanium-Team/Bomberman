package bomberman.gameplay;

public class Bomb extends TileObject {

    public Bomb(Tile parent, int lifespan) {
        super(parent, lifespan);
    }

    @Override
    public void execute() {
        //@TODO Implement
    }

    @Override
    public void interact(Player player) {}

}
