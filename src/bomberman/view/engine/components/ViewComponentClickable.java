package bomberman.view.engine.components;


import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewComponentClickable extends ViewComponent {

    public enum State {
        Default, Pressed, Hover;
    }

    protected boolean clickable = true;
    protected List<ClickListener> listeners = new ArrayList<>();
    protected State state = State.Default;

    public ViewComponentClickable(LayoutParams params, View v) {
        super(params, v);
    }

    protected void updateState() {
        if (Utility.viewComponentIsCollidingWithMouse(this, Mouse.getX(), Display.getHeight() - Mouse.getY()) && state != State.Pressed) {
            state = State.Hover;
        } else if (state == State.Hover){
            state = State.Default;
        }
    }

    @Override
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY) && this.clickable) {
            if (button == 0) {
                state = State.Pressed;
            }
        } else {
            state = State.Default;
        }
    }

    @Override
    public void onMouseUp(int button, int mouseX, int mouseY) {
        super.onMouseUp(button, mouseX, mouseY);

        if (button == 0 && state == State.Pressed) {
            state = State.Default;
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onClick();
            }
        }
    }

    public List<ClickListener> getListeners() {
        return listeners;
    }

    public void addListener(ClickListener listener) {
        this.listeners.add(listener);
    }
}
