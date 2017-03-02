package bomberman.view.engine;


import bomberman.view.engine.rendering.Batch;

public class TestView extends View {

    private float rotationRadians = 0;

    public TestView(float width, float height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        batch.draw(ViewManager.getTexture("test0.png"), 100, 100, 100, 100);

        int size = 20;
        for (int y = 450; y < getHeight() - size; y += size) {
            for (int x = 0; x < getWidth() - size; x += size) {
                batch.draw(ViewManager.getTexture("test0.png"), x, y, size, size, size / 2, size / 2, rotationRadians, 1f, 1f, 1f, 1f);
            }
        }
        ViewManager.font.drawText(batch, "Hallo Bomberman!     abcdefghijklmnopqrstuvwxyzß", 100, 400);

        rotationRadians += Math.toRadians(deltaTime * 90);

        super.render(deltaTime, batch);
    }

    @Override
    public void layout(float width, float height) {
        super.layout(width, height);
    }

}
