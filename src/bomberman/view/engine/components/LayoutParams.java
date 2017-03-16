package bomberman.view.engine.components;

public final class LayoutParams {

    public static LayoutParams obtain(float x, float y, float w, float h) {
        return new LayoutParams(x, y, w, h);
    }

    public final float x;
    public final float y;
    public final float w;
    public final float h;

    public LayoutParams(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

}
