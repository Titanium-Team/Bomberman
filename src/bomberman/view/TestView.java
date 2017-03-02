package bomberman.view;


import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

public class TestView extends View {

    public TestView(float width, float height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        ViewManager.font.drawText(batch, "Hallo Bomberman!     abcdefghijklmnopqrstuvwxyzß", 300, 5);

        int size = 50;
        for (int y = 50; y < getHeight(); y += size * 3) {
            for (int x = 0; x < getWidth(); x += size * 3) {
                batch.draw(null, x, y, size, size, size / 2, size / 2, 0f, 1f, 1f, 1f, 1f);
            }
        }

        super.render(deltaTime, batch);
    }

    @Override
    public void layout(float width, float height) {
        super.layout(width, height);
    }

}
