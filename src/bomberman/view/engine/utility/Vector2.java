package bomberman.view.engine.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Vector2 {

    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(String json){
        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, Float>>() {}.getType();

        Map<String, Float> vectorMap = gson.fromJson(json, type);

        this.x = vectorMap.get("xCoord");
        this.y = vectorMap.get("yCoord");

    }

    public float getLength() {
        return (float) Math.abs(Math.sqrt((x * x) + (y * y)));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void normalize() {
        float r = 1 / this.getLength();

        x *= r;
        y *= r;
    }

    public String toJson(){
        Gson gson = new Gson();

        Map<String, Float> data = new HashMap<>();
        data.put("xCoord", getX());
        data.put("yCoord", getY());

        return gson.toJson(data);
    }
}
