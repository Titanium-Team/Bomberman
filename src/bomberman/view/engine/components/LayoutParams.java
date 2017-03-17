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
        this.x = Math.max(0,Math.min(x,1));
        this.y = Math.max(0,Math.min(y,1));
        this.w = Math.max(0,Math.min(w,1));
        this.h = Math.max(0,Math.min(h,1));
    }

}
