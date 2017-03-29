package bomberman.gameplay.properties;

import bomberman.gameplay.Player;

import java.util.HashMap;
import java.util.Map;

public class PropertyRepository {

    private final Player player;
    private final Map<PropertyType, Entry> properties = new HashMap<>();

    public PropertyRepository(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public float getValue(PropertyType propertyType) {
        return this.properties.getOrDefault(propertyType, new Entry(propertyType.minValue(), propertyType.defaultValue(), propertyType.maxValue())).value;
    }

    public float getMin(PropertyType propertyType) {

        if(!(this.properties.containsKey(propertyType))) {
            this.setValue(propertyType, propertyType.defaultValue());
        }

        return this.properties.get(propertyType).min;

    }

    public float getMax(PropertyType propertyType) {

        if(!(this.properties.containsKey(propertyType))) {
            this.setValue(propertyType, propertyType.defaultValue());
        }

        return this.properties.get(propertyType).max;

    }

    public void setMin(PropertyType propertyType, float min) {

        if(this.properties.containsKey(propertyType)) {
            this.properties.get(propertyType).min = min;
            return;
        }

        this.setValue(propertyType, propertyType.defaultValue());

    }

    public void setMax(PropertyType propertyType, float max) {

        if(this.properties.containsKey(propertyType)) {
            this.properties.get(propertyType).max = max;
            return;
        }

        this.setValue(propertyType, propertyType.defaultValue());

    }

    public void setValue(PropertyType propertyType, float value) {

        if(this.properties.containsKey(propertyType)) {
            this.properties.get(propertyType).value = range(propertyType.minValue(), value, propertyType.maxValue());
            return;
        }

        this.properties.put(propertyType, new Entry(propertyType.minValue(), range(propertyType.minValue(), value, propertyType.maxValue()), propertyType.maxValue()));
    }

    public void reset(PropertyType propertyType) {
        this.properties.put(propertyType, new Entry(propertyType.minValue(), propertyType.defaultValue(), propertyType.maxValue()));
    }

    private static float range(float min, float value, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private class Entry {

        private Float min, value, max;

        public Entry(Float min, Float value, Float max) {
            this.min = min;
            this.value = value;
            this.max = max;
        }

        public Float getMax() {
            return this.max;
        }

        public Float getValue() {
            return this.value;
        }

        public Float getMin() {
            return this.min;
        }

    }

}
