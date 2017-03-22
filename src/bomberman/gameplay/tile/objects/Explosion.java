package bomberman.gameplay.tile.objects;

import bomberman.view.engine.ViewManager;

import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.TileObject;
import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.TileTypes;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Animation;
import bomberman.view.engine.rendering.Texture;

public class Explosion extends TileObject {

    private Animation animation;

    public Explosion(Tile parent, float lifespan) {
        super(parent, lifespan);

        this.animation = new Animation((Texture) ViewManager.getTexture("explosion.png"), 64, 64, Bomb.EXPLOSION_LIFESPAN / 25f);

    }

    @Override
    public void execute() {
        //@TODO Implement
        //System.out.println("Despawn explosion");
    }

    @Override
    public void interact(Player player) {
        if(player.getPropertyRepository().getValue(PropertyTypes.INVINCIBILITY) <= 0){
            player.loseHealth();
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean destroyWall() {

        if (this.getParent().getTileType() == TileTypes.WALL_BREAKABLE) {
            this.getParent().setTileType(TileTypes.GROUND);
            return true;
        }

        return false;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        animation.update(delta);
    }

}
