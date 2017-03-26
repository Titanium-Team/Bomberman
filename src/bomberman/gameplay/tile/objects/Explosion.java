package bomberman.gameplay.tile.objects;

import bomberman.gameplay.tile.TileObject;
import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileTypes;

public class Explosion extends TileObject{

    private final double damage;

    public Explosion(Tile parent, float lifespan, double damage) {
        super(parent, lifespan);

        this.damage = damage;
    }

    public double getDamage() {
        return this.damage;
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
            this.getParent().setHealth(this.getParent().getHealth() - this.damage);
            return true;
        }

        return false;

    }

}
