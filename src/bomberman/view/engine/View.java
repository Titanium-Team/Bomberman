package bomberman.view.engine;

import bomberman.Main;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.Panel;
import bomberman.view.engine.components.ViewComponent;
import bomberman.view.engine.components.ViewComponentClickable;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.util.ArrayList;
import java.util.List;

public abstract class View {

    protected Panel root;
    protected int width, height;
    protected final ViewManager viewManager;
    private View parentView = null;

    private Camera uiCamera;
    private Camera sceneCamera;

    private enum Dir {
        Left, Right, Up, Down;
    }

    private List<ViewComponentClickable> clickableList = new ArrayList<>();
    private ViewComponentClickable selectedClickable = null;
    private float selectorDelay = 0f;

    public View(int width, int height, ViewManager viewManager) {
        this.width = width;
        this.height = height;
        this.viewManager = viewManager;

        this.root = new Panel(LayoutParams.obtain(0, 0, 1, 1), this);
        this.root.layout(null);

        this.sceneCamera = new Camera(width, height);
        this.uiCamera = new Camera(width, height);
    }

    public void update(float deltaTime) {
        updateSelector();
    }

    private void updateSelector() {
        selectorDelay -= Main.instance.getLastDeltaTime();

        Controller gamepad = getViewManager().getSelectedGamepad();
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

    public final void render(Batch batch) {
        batch.setCombinedMatrix(sceneCamera.getCombined());
        renderScene(batch);

        batch.setCombinedMatrix(uiCamera.getCombined());
        renderUI(batch);
    }

    public void renderUI(Batch batch) {
        root.draw(batch);
    }

    public void renderScene(Batch batch) {
    }

    public void layout(int width, int height) {
        this.height = height;
        this.width = width;

        // this will relayout everything
        requestLayout();

        updateClickables();

        this.sceneCamera.resize(width, height);
        this.uiCamera.resize(width, height);
        this.uiCamera.setTranslation(new Vector2(width / 2, height / 2));
    }

    private void updateClickables() {
        clickableList.clear();

        List<ViewComponent> list = root.getDescendants();

        for (ViewComponent c : list) {
            if (c != null && c instanceof ViewComponentClickable) {
                clickableList.add((ViewComponentClickable) c);
            }
        }
    }

    public void requestLayout() {
        this.root.layout(null);
    }

    public Camera getSceneCamera() {
        return sceneCamera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Panel getRoot() {
        return root;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public void changeView(Class<? extends View> clazz) {
        changeView(ViewFactory.instance().createView(clazz, viewManager));
    }

    public void changeView(View newView) {
        if (newView != null) {
            newView.setParentView(this);

            viewManager.setCurrentView(newView);
        }
    }

    public void navigateBack() {
        View parent = this.getParentView();

        if (parent != null) {
            viewManager.setCurrentView(parent);
        }
    }

    public void onDestroy() {
    }

    public void onKeyDown(int key, char c) {
        root.onKeyDown(key, c);
    }

    public void onKeyUp(int key, char c) {
        root.onKeyUp(key, c);
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
        root.onMouseDown(button, mouseX, mouseY);
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
        root.onMouseUp(button, mouseX, mouseY);
    }

    public void onMouseWheel(int wheel) {
        root.onMouseWheel(wheel);
    }

    public void onGamepadEvent(Component component, float value) {
        if (component.getIdentifier() == Component.Identifier.Button._1 && value == 0) {
            this.navigateBack();
        }

        if (component.getIdentifier() == Component.Identifier.Button._0 && value == 0) {
            if (selectedClickable != null) {
                selectedClickable.simulateClick();
            }
        }

        root.onGamepadEvent(component, value);
    }
}
