package bomberman.gameplay.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Location implements Cloneable {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Location(String json){
        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, Double>>() {}.getType();

        Map<String, Double> vectorMap = gson.fromJson(json, type);

        this.x = vectorMap.get("x");
        this.y = vectorMap.get("y");

    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void substract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

    @Override
    public Location clone() {
        return new Location(this.x, this.y);
    }

    public String toJson(){
        Gson gson = new Gson();

        Map<String, Double> data = new HashMap<>();
        data.put("x", getX());
        data.put("y", getY());

        return gson.toJson(data);
    }

}
