package bomberman.view.engine.components;

import bomberman.Main;
import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ViewGroup extends ViewComponent {

    private enum Dir {
        Left, Right, Up, Down;
    }

    public List<ViewComponent> children;

    private List<ViewComponentClickable> clickableList = new ArrayList<>();
    private ViewComponentClickable selectedClickable = null;
    private float selectorDelay = 0f;

    public ViewGroup(LayoutParams params, View v) {
        super(params, v);
        this.children = new ArrayList<>();
    }

    @Override
    public void draw(Batch batch) {
        updateState();

        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(batch);
        }
    }

    private void updateState() {
        selectorDelay -= Main.instance.getLastDeltaTime();

        Controller gamepad = getView().getViewManager().getSelectedGamepad();
        if (gamepad != null && selectorDelay <= 0f) {
            float dx = gamepad.getComponent(Component.Identifier.Axis.X).getPollData();
            float dy = gamepad.getComponent(Component.Identifier.Axis.Y).getPollData();
            float offset = 0.2f;
            float delay = 0.3f;

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > offset) {
                    selectClickable(Dir.Right);
                    selectorDelay = delay;
                } else if (dx < -offset) {
                    selectClickable(Dir.Left);
                    selectorDelay = delay;
                }
            } else if (Math.abs(dy) > Math.abs(dx)) {
                if (dy > offset) {
                    selectClickable(Dir.Down);
                    selectorDelay = delay;
                } else if (dy < -offset) {
                    selectClickable(Dir.Up);
                    selectorDelay = delay;
                }
            }

        }
    }

    private void updateClickables() {
        clickableList.clear();

        List<ViewComponent> list = this.getChildren();

        for (ViewComponent c : list) {
            if (c != null && c instanceof ViewComponentClickable) {
                clickableList.add((ViewComponentClickable) c);
            }
        }
    }

    private void selectClickable(Dir dir) {
        if (selectedClickable != null) {
            selectedClickable.setSelected(false);
        }

        if (clickableList.size() > 0) {
            if (selectedClickable == null) {
                selectedClickable = clickableList.get(0);
            } else {
                int i = clickableList.indexOf(selectedClickable);

                if (dir == Dir.Up) {
                    i--;
                } else if (dir == Dir.Down) {
                    i++;
                }

                i = Math.max(0, Math.min(i, clickableList.size() - 1));

                selectedClickable = clickableList.get(i);
            }

            selectedClickable.setSelected(true);
        }
    }

    public void layout(ViewComponent parent) {
        super.layout(parent);

        for (int i = 0; i < children.size(); i++) {
            ViewComponent child = children.get(i);

            child.layout(this);
        }

        updateClickables();
    }

    public List<ViewComponent> getDescendants() {
        List<ViewComponent> descendants = new LinkedList<>();

        addDescendantsRecursive(descendants);

        return descendants;
    }

    private void addDescendantsRecursive(List<ViewComponent> list) {
        for (int i = 0; i < children.size(); i++) {
            ViewComponent child = children.get(i);

            list.add(child);

            if (child instanceof ViewGroup) {
                ((ViewGroup) child).addDescendantsRecursive(list);
            }
        }
    }

    public List<ViewComponent> getChildren() {
        return children;
    }

    public void addChild(ViewComponent child) {
        this.children.add(child);
    }

    public void removeChild(ViewComponent child) {
        this.children.remove(child);
    }

    public void onKeyDown(int key, char c) {
        for (ViewComponent v : children) {
            v.onKeyDown(key, c);
        }
    }

    public void onKeyUp(int key, char c) {
        for (ViewComponent v : children) {
            v.onKeyUp(key, c);
        }
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
        for (ViewComponent v : children) {
            v.onMouseDown(button, mouseX, mouseY);
        }
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
        for (ViewComponent v : children) {
            v.onMouseUp(button, mouseX, mouseY);
        }
    }

    public void onMouseWheel(int wheel) {
        for (ViewComponent v : children) {
            v.onMouseWheel(wheel);
        }
    }

    public void onGamepadEvent(Component component, float value) {
        if (component.getIdentifier() == Component.Identifier.Button._1 && value == 0) {
            getView().navigateBack();
        }

        if (component.getIdentifier() == Component.Identifier.Button._0 && value == 0) {
            if (selectedClickable != null) {
                selectedClickable.simulateClick();
            }
        }

        for (ViewComponent v : children) {
            v.onGamepadEvent(component, value);
        }
    }

}
