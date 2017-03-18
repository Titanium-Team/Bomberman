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
        //@TODO Implement
        System.out.println("PEWWWWW");
    }

    @Override
    public void interact(Player player) {
        System.out.println("DONT TOUCH MY TRALALA");
    }

    @Override
    public void update(float delta) {
        this.walkable.removeIf(player -> !(player.getBoundingBox().intersects(this.getParent().getBoundingBox())));
        super.update(delta);
    }

}
