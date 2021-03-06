package bomberman.view.engine.components;


import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
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
    private boolean selected = false;

    public ViewComponentClickable(LayoutParams params, View v) {
        super(params, v);
    }

    protected void updateState() {
        if ((Utility.viewComponentIsCollidingWithMouse(this, Mouse.getX(), Display.getHeight() - Mouse.getY()) || this.selected) && state != State.Pressed) {
            state = State.Hover;
        } else if (state == State.Hover) {
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

            playClickSound();
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onClick();
            }
        }
    }

    public void simulateClick() {
        playClickSound();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onClick();
        }
    }

    private void playClickSound() {
        ViewManager.clickSound.start();
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ClickListener> getListeners() {
        return listeners;
    }

    public void addListener(ClickListener listener) {
        this.listeners.add(listener);
    }
}
