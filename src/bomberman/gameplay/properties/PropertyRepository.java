package bomberman.gameplay.properties;

import bomberman.gameplay.Player;

import java.util.HashMap;
import java.util.Map;

public class PropertyRepository {

    private final Player player;
    private final Map<PropertyType, Object> properties = new HashMap<>();

    public PropertyRepository(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void set(PropertyType propertyType, float value) {
        this.properties.put(propertyType, value);
    }

    public <T> T get(PropertyType<T> propertyType) {
        return (T) this.properties.getOrDefault(propertyType, propertyType.defaultValue());
    }

    public void reset(PropertyType propertyType) {
        this.properties.put(propertyType, propertyType.defaultValue());
    }

}
