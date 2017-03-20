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

        switch (this.powerUpType) {

            case SPEEDUP: {
                repo.setValue(
                        PropertyTypes.SPEED_FACTOR,
                        repo.getValue(PropertyTypes.SPEED_FACTOR) + (float) PowerUpTypes.SPEEDUP.value()
                );
            }
            break;

            case SPEEDDOWN: {
                repo.setValue(
                    PropertyTypes.SPEED_FACTOR,
                    repo.getValue(PropertyTypes.SPEED_FACTOR) + (float) PowerUpTypes.SPEEDDOWN.value()
                );
            }
            break;

            case FIREUP: {
                repo.setValue(
                    PropertyTypes.BOMB_BLAST_RADIUS,
                    repo.getValue(PropertyTypes.BOMB_BLAST_RADIUS) + (int) PowerUpTypes.FIREUP.value()
                );
            }
            break;

            case FIREDOWN: {
                repo.setValue(
                    PropertyTypes.BOMB_BLAST_RADIUS,
                    repo.getValue(PropertyTypes.BOMB_BLAST_RADIUS) + (float) PowerUpTypes.FIREDOWN.value()
                );
            }
            break;

            case BOMBUP: {
                repo.setValue(
                    PropertyTypes.BOMB_AMOUNT,
                    repo.getValue(PropertyTypes.BOMB_AMOUNT) + (float) PowerUpTypes.BOMBUP.value()
                );
            }
            break;

            case BOMBDOWN: {
                repo.setValue(
                    PropertyTypes.BOMB_BLAST_RADIUS,
                    Math.max(1, repo.getValue(PropertyTypes.BOMB_BLAST_RADIUS) + (float) PowerUpTypes.BOMBDOWN.value())
                );
            }
            break;
        }

        this.parent.destroyObject();
    }

}
