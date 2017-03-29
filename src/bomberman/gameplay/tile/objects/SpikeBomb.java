package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;

/**
 * Created by Liam on 29.03.2017.
 */
public class SpikeBomb extends Bomb{
    public SpikeBomb(Player player, Tile parent, float lifespan, double damage){
        super(player, parent, lifespan, damage);
    }

    @Override
    public void execute() {

        //--- Add new bomb
        PropertyRepository repo = this.player.getPropertyRepository();
        repo.setValue(PropertyTypes.BOMB_AMOUNT, repo.getValue(PropertyTypes.BOMB_AMOUNT) + 1);
        repo.setValue(PropertyTypes.BOMBSDOWN, repo.getValue(PropertyTypes.BOMBSDOWN) - 1);


        //--- coordinates of the bomb
        int x = (int) this.getParent().getBoundingBox().getMin().getX();
        int y = (int) this.getParent().getBoundingBox().getMin().getY();


        //--- destroy the exploding bomb
        this.getParent().destroyObject();

        //--- create explosion
        this.createExplosion(x, y, EXPLOSION_LIFESPAN);


        //--- effect surrounding tiles

        for (int i = 1; i < this.range + 1; i++) {

            System.out.println(range + " = range");
            if ((x + i) < this.player.getGameSession().getGameMap().getWidth()) {
                this.createExplosion((x + i), y, EXPLOSION_LIFESPAN);
            }

            if ((x - i) > 0) {
                this.createExplosion((x - i), y, EXPLOSION_LIFESPAN);
            }

            if ((y + i) < this.player.getGameSession().getGameMap().getHeight()) {
                this.createExplosion(x, (y + i), EXPLOSION_LIFESPAN);
            }

            if ((y - i) > 0 ) {
                this.createExplosion(x, (y - i), EXPLOSION_LIFESPAN);
            }

        }

    }
}
