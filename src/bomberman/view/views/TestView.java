package bomberman.view.views;


import bomberman.view.engine.Light;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Mouse;

import java.util.Random;

public class TestView extends LightingView {

	private static final Random random = new Random();

	private Light draggingLight;

	private float time = 0f;

	public TestView(int width, int height, ViewManager viewManager) {
		super(width, height, viewManager);
	}

	public void update(float deltaTime) {
		time += deltaTime;

		if (draggingLight != null) {
			this.draggingLight.setX(Mouse.getX());
			this.draggingLight.setY(-Mouse.getY() + getHeight());
		}

		this.getSceneCamera().setTranslation(new Vector2(getWidth() / 2, getHeight() / 2));
	}

	@Override
	public void renderOccluders(Batch batch, Camera camera) {
	    /*int size = 50;
        for (int y = 50; y < getHeight(); y += size * 3) {
            for (int x = 0; x < getWidth(); x += size * 3) {
                batch.draw(null, x, y, size, size, size / 2, size / 2, 0f, 1f, 1f, 1f, 1f);
            }
        }*/

		ViewManager.font.drawText(batch, "Hallo Bomberman!     abcdefghijklmnopqrstuvwxyzß", 50, 300);

		//batch.draw(null, 300, 300, 100, 100, 100 / 2, 100 / 2, 0f, 1f, 1f, 1f, 1f);
	}

	@Override
	public void renderNonOccluders(Batch batch) {
		//ViewManager.font.drawText(batch, "Hallo Bomberman!     abcdefghijklmnopqrstuvwxyzß", 300, 5);
	}

	@Override
	public void layout(int width, int height) {
		super.layout(width, height);
	}

	public void onMouseDown(int button, int mouseX, int mouseY) {
		if (button == 0) {
			this.draggingLight = randomLight(getSceneCamera().getTranslation().getX() - (getSceneCamera().getWidth() / 2) + mouseX, getSceneCamera().getTranslation().getY() - (getSceneCamera().getHeight() / 2) + mouseY);

			this.addLight(draggingLight);
		}
		if (button == 1) {
			this.draggingLight = null;
			this.clearLights();
		}
	}

	public void onMouseUp(int button, int mouseX, int mouseY) {
		if (button == 0) {
			this.draggingLight = null;
		}
	}

	private Light randomLight(float x, float y) {
		float r = random.nextFloat() / 2 + 0.5f;
		float g = random.nextFloat() / 2 + 0.5f;
		float b = random.nextFloat() / 2 + 0.5f;

		return new Light(x, y, 400, r, g, b);
	}

}
