package bomberman.gameplay.tile.objects;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

import java.util.ArrayList;
import java.util.List;


public class Bomb extends TileObject {


    public final static float EXPLOSION_LIFESPAN = 1F;

    private List<Player> walkable = new ArrayList<>();
    private Player player;

    private final int range;
    private final double damage;

    public Bomb(Player player, Tile parent, float lifespan, double damage) {

        super(parent, lifespan);

        this.player = player;
        this.range = (int) this.player.getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS);
        this.damage = damage;

        Main.instance.getGameplayManager().getPlayers().forEach(e -> {
            if (e.getBoundingBox().intersects(this.getParent().getBoundingBox())) {
                this.walkable.add(e);
            }
        });
    }

    public int getRange() {
        return this.range;
    }

    public boolean canVisit(Player p) {
        return this.walkable.contains(p);
    }

    @Override
    public void execute() {

        //--- Add new bomb
        PropertyRepository repo = this.player.getPropertyRepository();
        repo.setValue(PropertyTypes.BOMB_AMOUNT, repo.getValue(PropertyTypes.BOMB_AMOUNT) + 1);

        //--- coordinates of the bomb
        int x = (int) this.getParent().getBoundingBox().getMin().getX();
        int y = (int) this.getParent().getBoundingBox().getMin().getY();


        //--- destroy the exploding bomb
        this.getParent().destroyObject();

        //--- create explosion
        this.createExplosion(x, y, EXPLOSION_LIFESPAN);


        //--- effect surrounding tiles
        boolean stopUp = false, stopLeft = false, stopDown = false, stopRight = false;

        for (int i = 1; i < this.range + 1; i++) {

            if ((x + i) < this.player.getGameMap().getWidth() && (!stopRight)) {
                stopRight = this.createExplosion((x + i), y, EXPLOSION_LIFESPAN);
            }

            if ((x - i) > 0 && !stopLeft) {
                stopLeft = this.createExplosion((x - i), y, EXPLOSION_LIFESPAN);
            }

            if ((y + i) < this.player.getGameMap().getHeight() && !stopUp) {
                stopUp = this.createExplosion(x, (y + i), EXPLOSION_LIFESPAN);
            }

            if ((y - i) > 0 && !stopDown) {
                stopDown = this.createExplosion(x, (y - i), EXPLOSION_LIFESPAN);
            }

        }

    }

    @Override
    public void interact(Player player) {
    }

    @Override
    public void update(float delta) {
        this.walkable.removeIf(player -> !(player.getBoundingBox().intersects(this.getParent().getBoundingBox())));
        super.update(delta);
    }

    private boolean createExplosion(int x, int y, float lifespan) {

        Explosion explosion = new Explosion(this.player, this.player.getGameMap().getTile(x, y).get(), lifespan, this.damage);

        //--- spawning explosion
        if(this.player.getGameMap().getTile(x,y).get().getTileObject() instanceof Bomb) {
            this.player.getGameMap().getTile(x,y).get().getTileObject().execute();
        }

        this.player.getGameMap().getTile(x, y).get().spawn(explosion);

        return explosion.destroyWall();

    }

}
