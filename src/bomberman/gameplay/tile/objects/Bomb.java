package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

public class Bomb extends TileObject {

    public Bomb(Player owner, Tile parent, int lifespan) {
        super(owner, parent, lifespan);
    }

    @Override
    public void execute() {
        //@TODO Implement
        System.out.println("PEWWWWW");
    }

    @Override
    public void interact(Player player) {
        System.out.println("DONT TOUCH MY TRALALA");
    }

}
