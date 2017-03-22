package bomberman.view.engine;

import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.Panel;
import bomberman.view.engine.components.ViewComponent;
import bomberman.view.engine.components.ViewComponentClickable;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import net.java.games.input.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class View {

    protected Panel root;
    protected int width, height;
    protected final ViewManager viewManager;
    private View parentView = null;

    private List<ViewComponentClickable> clickableList = new ArrayList<>();
    private ViewComponentClickable selectedClickable = null;

    private Camera uiCamera;
    private Camera sceneCamera;

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

        this.sceneCamera.resize(width, height);
        this.uiCamera.resize(width, height);
        this.uiCamera.setTranslation(new Vector2(width / 2, height / 2));

        updateClickables();
    }

    private void updateClickables() {
        clickableList.clear();

        List<ViewComponent> descendants = root.getDescendants();

        for (ViewComponent c : descendants) {
            if (c != null && c instanceof ViewComponentClickable) {
                clickableList.add((ViewComponentClickable) c);
            }
        }

        selectClickable(clickableList.get(0));
    }

    private void selectClickable(ViewComponentClickable clickable) {
        if (selectedClickable != null)
            selectedClickable.setSelected(false);

        selectedClickable = clickable;
        selectedClickable.setSelected(true);
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

    public void onGamepadEvent(Component component, float value) {
        if (component.getIdentifier() == Component.Identifier.Button._1 && value == 0) {
            navigateBack();
        }

        if (component.getIdentifier() == Component.Identifier.Button._0 && value == 0) {
            if (selectedClickable != null) {
                selectedClickable.simulateClick();
            }
        }

        root.onGamepadEvent(component, value);
    }
}
