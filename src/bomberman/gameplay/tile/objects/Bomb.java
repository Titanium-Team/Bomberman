package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

public class Bomb extends TileObject {

    private float triggerTimer;

    public Bomb(Player owner, Tile parent, float lifespan, float triggerTimer) {
        super(owner, parent, lifespan);

        this.triggerTimer = triggerTimer;
    }

    public boolean isTriggered() {
        return (this.triggerTimer <= 0);
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

        //--- unlock timer
        if(this.triggerTimer > 0) {
            this.triggerTimer -= delta;
            return;
        }

        super.update(delta);
    }
}
