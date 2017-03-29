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

import java.util.Random;

public abstract class BaseMenuView extends LightingView {

    private static final long seed = System.nanoTime() * 1337;//random at its finests

    protected final Button backButton;

    private final Light demoLight;
    private boolean[][] demoGrid;
    private int gridSize = 20;

    public BaseMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.backButton = new Button(LayoutParams.obtain(0.05f, 0.05f, 0.1f, 0.1f), this, "Back");
        this.backButton.addListener(() -> BaseMenuView.this.navigateBack());
        this.getRoot().addChild(backButton);

        this.demoLight = new Light(0, 0, 400, 1f, 1f, 1f);
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

        genDemoGrid();

        this.getSceneCamera().setTranslation(new Vector2(getWidth() / 2, getHeight() / 2));
    }

    private void genDemoGrid() {
        Random r = new Random(seed);

        demoGrid = new boolean[(getWidth() / gridSize) + 1][(getHeight() / gridSize) + 1];
        for (int x = 0; x < demoGrid.length; x++) {
            for (int y = 0; y < demoGrid[x].length; y++) {
                demoGrid[x][y] = r.nextFloat() > 0.9f;
            }
        }
    }

    public void renderOccluders(Batch batch, Camera camera) {
        for (int x = 0; x < demoGrid.length; x++) {
            for (int y = 0; y < demoGrid[x].length; y++) {
                if (demoGrid[x][y] == true) {
                    batch.draw(null, x * gridSize, y * gridSize, gridSize + 1, gridSize + 1, 0f, 0f, 0f, 1f);
                }
            }
        }
    }

    public void renderNonOccluders(Batch batch, Camera camera) {
    }

}