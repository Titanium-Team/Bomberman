package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

public class Bomb extends TileObject {

    public Bomb(Tile parent, int lifespan) {
        super(parent, lifespan);
    }

    @Override
    public void execute() {
        //@TODO Implement
    }

    @Override
    public void interact(Player player) {
    }

}
