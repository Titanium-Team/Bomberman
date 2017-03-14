package bomberman.view.engine.utility;

public class Vector2 {

    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
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
}
