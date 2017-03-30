package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Animation;
import bomberman.view.engine.rendering.Texture;

public class Explosion extends TileObject {

    private Player owner;
    private Animation animation;
    private final double damage;


    public Explosion(Player owner, Tile parent, float lifespan, double damage) {
        super(parent, lifespan);

        this.damage = damage;
        this.owner = owner;
        this.animation = new Animation((Texture) ViewManager.getTexture("explosion.png"), 64, 64, Bomb.EXPLOSION_LIFESPAN / 25f);


    }

    @Override
    public void execute() {}

    @Override
    public void interact(Player player) {

        //---
        BoundingBox playerBox = player.getBoundingBox();
        BoundingBox explosionBox = this.getParent().getBoundingBox();

        double x_overlap = Math.max(0, Math.min(playerBox.getMax().getX(), explosionBox.getMax().getX()) - Math.max(playerBox.getMin().getX(), explosionBox.getMin().getX()));
        double y_overlap = Math.max(0, Math.min(playerBox.getMax().getY(), explosionBox.getMax().getY()) - Math.max(playerBox.getMin().getY(), explosionBox.getMin().getY()));

        if((x_overlap * y_overlap) > .2D) {
            if (player.getPropertyRepository().getValue(PropertyTypes.INVINCIBILITY) <= 0) {
                player.loseHealth();

                if (!(player == this.owner)) {
                    this.owner.getGameStatistic().update(Statistics.KILLS, 1);
                } else {
                    this.owner.getGameStatistic().update(Statistics.SUICIDES, 1);
                }
            }
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean destroyWall() {

        if (this.getParent().getTileType().isDestroyable()) {

            this.getParent().setHealth(this.getParent().getHealth() - this.damage);

            if (this.getParent().getTileType() == TileTypes.WALL_BREAKABLE_IMPROVBED) {

            }
            if(this.getParent().getHealth() <= 0) {
                this.owner.getGameStatistic().update(Statistics.DESTROYED_WALLS, 1);
                this.getParent().setTileType(TileTypes.GROUND);
                int random = (int) (Math.random() * 3);
                if(random== 0){
                    this.getParent().spawnPowerup(15);
                }
            } else if (this.getParent().getTileType() == TileTypes.WALL_BREAKABLE_IMPROVBED) {
                this.getParent().setTileType(TileTypes.WALL_BREAKABLE);
            }

            return true;
        }

        return false;

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.animation.update(delta);
    }

}
