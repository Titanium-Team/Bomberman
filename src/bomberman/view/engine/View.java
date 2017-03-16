package bomberman.view.engine;

import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.Panel;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;

public abstract class View {

    protected Panel root;
    protected int width, height;
    protected final ViewManager viewManager;
    private View parentView = null;

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
        this.root.layout(null);

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
}
