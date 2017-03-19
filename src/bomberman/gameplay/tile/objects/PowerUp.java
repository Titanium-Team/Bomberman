package bomberman.gameplay.tile.objects;

import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;

public class PowerUp extends TileObject {

    private Tile parent;
    private PowerUpTypes powerUpType;

    public PowerUp(Tile parent, float lifespan, PowerUpTypes pPowerUpType) {
        super(parent, lifespan);

        this.parent = parent;
        this.powerUpType = pPowerUpType;
    }

    @Override
    public void execute() {
        this.parent.destroyObject();
    }

    @Override
    public void interact(Player player) {

        PropertyRepository repo = player.getPropertyRepository();

        switch (powerUpType) {

            case SPEEDUP:
                repo.set(
                        PropertyTypes.SPEED_FACTOR,
                        repo.<Float>get(PropertyTypes.SPEED_FACTOR) + (float) powerUpType.value()
                );

                System.out.println("SpeedUP");
                System.out.println(repo.get(PropertyTypes.SPEED_FACTOR));
                break;

            case SPEEDDOWN:
                /*if(player.getPLAYER_speedFactor()>((float)powerUpType.SPEEDDOWN.value()*-1)){
                    player.setPLAYER_speedFactor(player.getPLAYER_speedFactor()+(float)powerUpType.SPEEDDOWN.value());
                    System.out.println("SpeedDOWN");
                }*/
                break;

            case FIREUP:
                repo.set(
                        PropertyTypes.BOMB_BLAST_RADIUS,
                        repo.<Integer>get(PropertyTypes.BOMB_BLAST_RADIUS) + (int) powerUpType.value()
                );

                System.out.println("FireUP");
                System.out.println(repo.get(PropertyTypes.BOMB_BLAST_RADIUS));
                break;

            case FIREDOWN:
                /*if(player.getBOMB_blastRadius()>1){
                    player.setBOMB_blastRadius(player.getBOMB_blastRadius()+(int)powerUpType.FIREDOWN.value());
                    System.out.println("FireDOWN");
                }*/
                break;

            case BOMBUP:
                repo.set(
                        PropertyTypes.BOMB_AMOUNT,
                        repo.<Integer>get(PropertyTypes.BOMB_AMOUNT) + (int) powerUpType.value()
                );

                System.out.println("BombUP");
                System.out.println(repo.get(PropertyTypes.BOMB_AMOUNT));
                break;

            case BOMBDOWN:
                /*if(player.getBOMB_amount()>1){
                    player.setBOMB_amount(player.getBOMB_amount()+(int)powerUpType.BOMBDOWN.value());
                    System.out.println("BombDOWN");
                }*/
                break;
        }

        this.parent.destroyObject();
    }

}
