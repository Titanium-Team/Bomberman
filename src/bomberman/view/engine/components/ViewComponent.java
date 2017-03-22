package bomberman.view.engine.components;


import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import net.java.games.input.Component;

public abstract class ViewComponent {

    private View view;
    private LayoutParams params;
    private float x, y, width, height;
    private boolean visible;


    public ViewComponent(LayoutParams params, View v) {
        this.params = params;
        this.view = v;
        this.visible = true;
    }

    public abstract void draw(Batch batch);

    public void layout(ViewComponent parent) {
        if (parent == null) {
            this.x = 0;
            this.y = 0;
            this.width = view.getWidth();
            this.height = view.getHeight();
        } else {
            this.x = parent.getX() + (parent.getWidth() * params.x);
            this.y = parent.getY() + (parent.getHeight() * params.y);
            this.width = parent.getWidth() * params.w;
            this.height = parent.getHeight() * params.h;
        }
    }


    public View getView() {
        return view;
    }

    public LayoutParams getParams() {
        return params;
    }

    public void setParams(LayoutParams params) {
        this.params = params;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // callbacks

    public void onKeyDown(int key, char c) {
    }

    public void onKeyUp(int key, char c) {
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
    }

    public void onGamepadEvent(Component component, float value) {
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
