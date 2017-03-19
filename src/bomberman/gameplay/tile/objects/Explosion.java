package bomberman.gameplay.tile.objects;

import bomberman.gameplay.tile.TileObject;
import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileTypes;

public class Explosion extends TileObject{

    public Explosion(Tile parent, float lifespan) {
        super(parent, lifespan);

    }
    @Override
    public void execute() {
        //@TODO Implement
        //System.out.println("Despawn explosion");
    }

    @Override
    public void interact(Player player) {
        System.out.println("player dead");
    }

    public boolean destroyWall(){

        if(this.getParent().getTileType() == TileTypes.WALL_BREAKABLE){
            this.getParent().setTileType(TileTypes.GROUND);
            return true;
        }

        return false;

    }

}
