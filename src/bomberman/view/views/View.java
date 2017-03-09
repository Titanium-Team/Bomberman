package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.ViewComponent;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class View {

    protected List<ViewComponent> components;
    protected int width, height;
    protected final ViewManager viewManager;

    private Camera uiCamera;
    private Camera sceneCamera;

    public View(int width, int height, ViewManager viewManager) {
        components = new ArrayList<ViewComponent>();
        this.height = height;
        this.width = width;
        this.viewManager = viewManager;

        this.sceneCamera = new Camera(width, height);
        this.uiCamera = new Camera(width, height);
        this.uiCamera.setTranslation(new Vector2(width / 2, height / 2));
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
    }

    public void renderScene(Batch batch) {
    }

    public void layout(int width, int height) {
        this.height = height;
        this.width = width;

        this.sceneCamera.resize(width, height);
        this.uiCamera.resize(width, height);
        this.uiCamera.setTranslation(new Vector2(width / 2, height / 2));
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

    public List<ViewComponent> getComponents() {
        return components;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public void onKeyDown(int key, char c) {
        for (ViewComponent v : components) {
            v.onKeyDown(key, c);
        }
    }

    public void onKeyUp(int key, char c) {
        for (ViewComponent v : components) {
            v.onKeyUp(key, c);
        }
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
        for (ViewComponent v : components) {
            v.onMouseDown(button, mouseX, mouseY);
        }
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
        for (ViewComponent v : components) {
            v.onMouseUp(button, mouseX, mouseY);
        }
    }
}
