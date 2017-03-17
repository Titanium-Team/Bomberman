package bomberman.gameplay.tile.objects;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomb extends TileObject {

    private float triggerTimer;
    private List<Player> walkable = new ArrayList<>();

    public Bomb(Tile parent, float lifespan, float triggerTimer) {
        super(parent, lifespan);

        Main.instance.getGameplayManager().getPlayers().forEach(e -> {

            if (e.getBoundingBox().intersects(this.getParent().getBoundingBox())){
                walkable.add(e);
            }
        });
    }

    public boolean canVisit(Player p){
        return walkable.contains(p);
    }

    public boolean isSolid() {

        for (Player p : Main.instance.getGameplayManager().getPlayers()){

            if (p.getBoundingBox().intersects(this.getParent().getBoundingBox())){
                return false;
            }
        }
        System.out.println("Rock");
        return true;
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

    @Override
    public void update(float delta) {
        Iterator<Player> iterator = walkable.iterator();

        while(iterator.hasNext()){
            if (!(iterator.next().getBoundingBox().intersects(this.getParent().getBoundingBox()))){
                iterator.remove();
            }
        }
        super.update(delta);
    }

}
