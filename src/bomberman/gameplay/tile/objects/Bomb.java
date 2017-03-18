package bomberman.gameplay.tile.objects;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.utils.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomb extends TileObject {

    private final static float EXPLOSION_LIFESPAN = 1F;

    private List<Player> walkable = new ArrayList<>();
    private Player player;

    public Bomb(Player player, Tile parent, float lifespan) {

        super(parent, lifespan);
        this.player = player;

        Main.instance.getGameplayManager().getPlayers().forEach(e -> {

            if (e.getBoundingBox().intersects(this.getParent().getBoundingBox())) {
                this.walkable.add(e);
            }
        });
    }

    public boolean canVisit(Player p) {
        return walkable.contains(p);
    }

    @Override
    public void execute() {

        //--- destroy the exploding bomb
        this.getParent().destroyObject();

        //--- coordinates of the bomb
        int x = (int) this.getParent().getBoundingBox().getMin().getX();
        int y = (int) this.getParent().getBoundingBox().getMin().getY();

        //--- create explosion
        this.createExplosion(x, y, 1);

        //--- effect surrounding tiles
        boolean stopUp = false,stopLeft= false,stopDown = false,stopRight = false;

        for(int i = 1; i < this.player.getPropertyRepository().<Integer>get(PropertyTypes.BOMB_BLAST_RADIUS) + 1; i++){

            if((x + i) < this.player.getGameMap().getWidth() && !stopRight ){
                stopRight = this.createExplosion((x + i), y, EXPLOSION_LIFESPAN);
            }

            if((x - i) > 1 && !stopLeft){
                stopLeft = this.createExplosion((x - i), y, EXPLOSION_LIFESPAN);
            }

            if((y + i) < this.player.getGameMap().getHeight() && !stopUp) {
                stopUp = this.createExplosion(x, (y + i), EXPLOSION_LIFESPAN);
            }

            if((y-i) > 1 && !stopDown) {
                stopDown = this.createExplosion(x, (y - i), EXPLOSION_LIFESPAN);
            }

        }

    }

    @Override
    public void interact(Player player) {}

    @Override
    public void update(float delta) {
        this.walkable.removeIf(player -> !(player.getBoundingBox().intersects(this.getParent().getBoundingBox())));
        super.update(delta);
    }

    private boolean createExplosion(int x, int y, float lifespan){

        Explosion explosion = new Explosion(this.player.getGameMap().getTile(x, y), lifespan);

        //--- spawning explosion
        if(this.player.getGameMap().getTile(x,y).getTileObject() instanceof Bomb) {
            this.player.getGameMap().getTile(x,y).getTileObject().execute();
        }

        this.player.getGameMap().getTile(x, y).spawn(explosion);

        return explosion.destroyWall();

    }

}
