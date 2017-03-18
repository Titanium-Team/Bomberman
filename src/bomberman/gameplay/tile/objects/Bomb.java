package bomberman.gameplay.tile.objects;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomb extends TileObject {

    private float triggerTimer;
    private List<Player> walkable = new ArrayList<>();
    //player der die bombe legt
    private Player player;
    private int x;
    private int y;

    public Bomb(Tile parent, float lifespan, Player player, int x, int y) {
        super(parent, lifespan);
        this.player = player;
        this.x = x;
        this.y = y;

        Main.instance.getGameplayManager().getPlayers().forEach(e -> {

            if (e.getBoundingBox().intersects(this.getParent().getBoundingBox())) {
                walkable.add(e);
            }
        });
    }

    public boolean canVisit(Player p) {
        return walkable.contains(p);
    }

    @Override
    public void execute() {
        //@TODO Implement
        getParent().destroyObject();
        //System.out.println("PEWWWWW");
        createExplosion(x,y,1);
        boolean stopUp = false,stopLeft= false,stopDown = false,stopRight = false;
        for(int i = 1; i < player.getPropertyRepository().<Integer>get(PropertyTypes.BOMB_BLAST_RADIUS)+1; i++){
            if(x+i <player.getGameMap().getWidth()&& !stopRight ){
                stopRight = createExplosion(x+i, y, 1);
            }
            if(x-i >1&& !stopLeft){
                stopLeft = createExplosion(x-i, y, 1);
            }
            if(y+i<player.getGameMap().getHeight()&& !stopUp) {
                stopUp = createExplosion(x, y + i, 1);
            }
            if(y-i>1 && !stopDown) {
                stopDown=createExplosion(x, y - i, 1);
            }
        }

    }

    @Override
    public void interact(Player player) {
        //System.out.println("DONT TOUCH MY TRALALA");
    }

    @Override
    public void update(float delta) {
        Iterator<Player> iterator = walkable.iterator();

        while (iterator.hasNext()) {
            if (!(iterator.next().getBoundingBox().intersects(this.getParent().getBoundingBox()))) {
                iterator.remove();
            }
        }
        super.update(delta);
    }

    private boolean createExplosion(int x, int y, int lifespan){
        Explosion tmp = new Explosion(player.getGameMap().getTile(x, y),lifespan);
        spawnExplosion(x,y,tmp);
        return tmp.destroyWall();
    }
    private void spawnExplosion(int x, int y,  Explosion explosion){
        if(player.getGameMap().getTile(x,y).getTileObject() instanceof Bomb) {
            player.getGameMap().getTile(x,y).getTileObject().execute();
        }
        player.getGameMap().getTile(x, y).spawn(explosion);
    }
}
