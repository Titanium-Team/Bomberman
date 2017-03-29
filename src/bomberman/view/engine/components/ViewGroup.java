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

    public List<ViewComponent> children;

    public ViewGroup(LayoutParams params, View v) {
        super(params, v);
        this.children = new ArrayList<>();
    }

    @Override
    public void draw(Batch batch) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(batch);
        }
    }

    public void layout(ViewComponent parent) {
        super.layout(parent);

        for (int i = 0; i < children.size(); i++) {
            ViewComponent child = children.get(i);

            child.layout(this);
        }
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

    public void removeAllChildren(){
        this.children.clear();
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
        for (ViewComponent v : children) {
            v.onGamepadEvent(component, value);
        }
    }

}
