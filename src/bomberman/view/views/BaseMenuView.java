package bomberman.view.views;

import bomberman.view.engine.Light;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Mouse;

public abstract class BaseMenuView extends LightingView {

    protected final Button backButton;

    private final Light demoLight;

    public BaseMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.backButton = new Button(LayoutParams.obtain(0.05f, 0.05f, 0.1f, 0.1f), this, "Back");
        this.backButton.addListener(() -> BaseMenuView.this.navigateBack());
        this.getRoot().addChild(backButton);

        this.demoLight = new Light(0, 0, 300, 1f, 1f, 1f);
        this.addLight(demoLight);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        demoLight.setX(Mouse.getX());
        demoLight.setY(-Mouse.getY() + getHeight());
    }

    @Override
    public void layout(int width, int height) {
        super.layout(width, height);

        this.getSceneCamera().setTranslation(new Vector2(getWidth() / 2, getHeight() / 2));
    }

    public void renderOccluders(Batch batch, Camera camera) {
    }

    public void renderNonOccluders(Batch batch, Camera camera) {
    }

}