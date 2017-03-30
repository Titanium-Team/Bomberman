package bomberman.gameplay.tile.objects;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyRepository;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.Statistic;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.utils.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PowerUp extends TileObject {

    private Tile parent;
    private PowerUpTypes powerUpType;

    public PowerUp( Tile parent, float lifespan, PowerUpTypes powerUpType) {
        super(parent, lifespan);

        this.parent = parent;
        this.powerUpType = powerUpType;

        Main.instance.getNetworkController().powerUpSpawn(this);
    }

    @Override
    public void execute() {
        this.parent.destroyObject();
    }

    @Override
    public void interact(Player player) {

        PropertyRepository repo = player.getPropertyRepository();
        player.getGameStatistic().update(Statistics.COLLECTED_POWERUPS, 1);

        player.setLastPowerup(this.powerUpType);

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

            case POWERBOMB: {
                repo.setValue(
                        PropertyTypes.BOMBTYPE,
                        (float)PowerUpTypes.POWERBOMB.value()
                );
                System.out.println("picked up powerbomb");
            }
            break;

            case SPIKEBOMB: {
                repo.setValue(
                        PropertyTypes.BOMBTYPE,
                        (float)PowerUpTypes.SPIKEBOMB.value()
                );
                System.out.println("picked up spikebomb");
            }
            break;

        }

        this.parent.destroyObject();
    }

    public PowerUpTypes getPowerUpType() {
        return powerUpType;
    }

    @Override
    public PowerUp clone(Tile parent) {
        return new PowerUp(parent, this.getLifespan(), this.powerUpType);
    }

    public static PowerUp fromJson(String json){
        Gson gson = new Gson();
        Type typeMap = new TypeToken<Map<String, String>>(){}.getType();

        Map<String, String> jsonMap = gson.fromJson(json, typeMap);

        Type typeType = new TypeToken<PowerUpTypes>(){}.getType();

        Location location = new Location(jsonMap.get("location"));
        Tile tile = Main.instance.getGameplayManager().getCurrentSession().getGameMap().getTile((int) location.getX(), (int) location.getY()).get();
        return new PowerUp(tile, Float.parseFloat(jsonMap.get("lifeSpan")), gson.fromJson(jsonMap.get("type"), typeType));
    }

    public String toJson() {
        Gson gson = new Gson();

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("location", getParent().getBoundingBox().getCenter().toJson());
        jsonMap.put("lifeSpan", String.valueOf(getLifespan()));
        jsonMap.put("type", gson.toJson(powerUpType));

        return gson.toJson(jsonMap);
    }
}
