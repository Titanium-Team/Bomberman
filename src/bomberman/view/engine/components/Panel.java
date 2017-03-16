package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;

public class Panel extends ViewGroup {

    private float r, g, b, a;

    public Panel(LayoutParams params, View v) {
        super(params, v);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(null, getX(), getY(), getWidth(), getHeight(), r, g, b, a);

        super.draw(batch);
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
