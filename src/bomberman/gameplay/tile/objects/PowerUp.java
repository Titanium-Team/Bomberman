package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.objects.PowerUpTypes;

/**
 * Created by 204g04 on 17.03.2017.
 */
public class PowerUp extends TileObject{
    private PowerUpTypes powerUpType;
    private Tile parent;
    public PowerUp(Tile parent, float lifespan, PowerUpTypes pPowerUpType){
        super(parent, lifespan);
        this.parent = parent;
        powerUpType = pPowerUpType;

    }
    @Override
    public void execute() {
        //@TODO Implement
        System.out.println("Despawn");
    }

    @Override
    public void interact(Player player) {
        switch (powerUpType){
            case SPEEDUP:
                player.setPLAYER_speedFactor(player.getPLAYER_speedFactor()+(float)powerUpType.SPEEDUP.value());
                System.out.println("SpeedUP");
            case SPEEDDOWN:
                if(player.getPLAYER_speedFactor()>((float)powerUpType.SPEEDDOWN.value()*-1)){
                    player.setPLAYER_speedFactor(player.getPLAYER_speedFactor()+(float)powerUpType.SPEEDDOWN.value());
                    System.out.println("SpeedDOWN");
                }
            case FIREUP:
                player.setBOMB_blastRadius(player.getBOMB_blastRadius()+(int)powerUpType.FIREUP.value());
                System.out.println("FireUP");
            case FIREDOWN:
                if(player.getBOMB_blastRadius()>1){
                    player.setBOMB_blastRadius(player.getBOMB_blastRadius()+(int)powerUpType.FIREDOWN.value());
                    System.out.println("FireDOWN");
                }
            case BOMBUP:
                player.setBOMB_amount(player.getBOMB_amount()+(int)powerUpType.BOMBUP.value());
                System.out.println("BombUP");
            case BOMBDOWN:
                if(player.getBOMB_amount()>1){
                    player.setBOMB_amount(player.getBOMB_amount()+(int)powerUpType.BOMBDOWN.value());
                    System.out.println("BombDOWN");
                }
        }
        parent.destroyObject();
        System.out.println("DONT TOUCH MY TRALALA");
    }

    @Override
    public void update(float delta) {


        super.update(delta);
    }
}
